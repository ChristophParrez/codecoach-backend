package be.codecoach.services;

import be.codecoach.api.dtos.FeedbackDto;
import be.codecoach.api.dtos.SessionDto;
import be.codecoach.domain.*;
import be.codecoach.exceptions.DatabaseException;
import be.codecoach.exceptions.FeedbackAlreadyProvidedException;
import be.codecoach.exceptions.ForbiddenAccessException;
import be.codecoach.exceptions.InvalidInputException;
import be.codecoach.repositories.*;
import be.codecoach.services.mappers.FeedbackMapper;
import be.codecoach.services.mappers.SessionMapper;
import be.codecoach.services.validators.SessionValidator;
import be.codecoach.twilio.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionService {

    private static final String FORBIDDEN_ACCESS_MESSAGE = "You cannot update sessions for somebody else";
    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
    public static final String STATUS_REQUESTED = "REQUESTED";
    public static final String STATUS_FINISHED_CANCELLED_BY_COACH = "FINISHED_CANCELLED_BY_COACH";
    public static final String STATUS_DONE_WAITING_FOR_FEEDBACK = "DONE_WAITING_FOR_FEEDBACK";
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_DECLINED = "DECLINED";
    public static final String STATUS_FINISHED_CANCELLED_BY_COACHEE = "FINISHED_CANCELLED_BY_COACHEE";
    public static final String STATUS_FINISHED = "FINISHED";
    public static final String STATUS_FINISHED_FEEDBACK_GIVEN = "FINISHED_FEEDBACK_GIVEN";
    public static final String STATUS_FINISHED_AUTOMATICALLY_CLOSED = "FINISHED_AUTOMATICALLY_CLOSED";

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final SessionValidator sessionValidator;
    private final SessionMapper sessionMapper;
    private final StatusRepository statusRepository;
    private final AuthenticationService authenticationService;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;
    private final SmsSender smsSender;

    @Autowired
    public SessionService(UserRepository userRepository, SessionRepository sessionRepository, SessionValidator sessionValidator, SessionMapper sessionMapper, StatusRepository statusRepository, AuthenticationService authenticationService, FeedbackMapper feedbackMapper, FeedbackRepository feedbackRepository, LocationRepository locationRepository, SmsSender smsSender) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.sessionValidator = sessionValidator;
        this.sessionMapper = sessionMapper;
        this.statusRepository = statusRepository;
        this.authenticationService = authenticationService;
        this.feedbackMapper = feedbackMapper;
        this.feedbackRepository = feedbackRepository;
        this.smsSender = smsSender;
    }

    public void requestSession(SessionDto sessionDto) {
        authenticationService.assertUserHasRightsToPerformAction(sessionDto.getCoacheeId(), FORBIDDEN_ACCESS_MESSAGE);

        sessionValidator.validateRawInput(sessionDto);

        Optional<User> coachInDatabase = userRepository.findById(sessionDto.getCoachId());
        Optional<User> coacheeInDatabase = userRepository.findById(sessionDto.getCoacheeId());
        sessionValidator.assertGivenIdsAreValid(coachInDatabase, coacheeInDatabase);

        sessionValidator.assertCoachHasRequestedTopic(coachInDatabase, sessionDto);

        Location location = sessionValidator.validateLocationMatchesGivenPossibilities(sessionDto.getLocation().getName());
        Status status = sessionValidator.getStatusFromRepository(STATUS_REQUESTED);

        Session sessionToBeSaved = sessionMapper.toEntity(sessionDto, coachInDatabase.get(), coacheeInDatabase.get(), status, location);

        sessionRepository.save(sessionToBeSaved);

        if (coacheeInDatabase.get().getTelephoneNumber() != null) {
            smsSender.sendMessage(coachInDatabase.get().getTelephoneNumber(), "A coachee requested a session on " + sessionDto.getDate() + " at " + sessionDto.getTime());
        }
    }


    public List<SessionDto> getSessions(String role) {
        String idFromDatabase = authenticationService.getAuthenticationIdFromDb();

        List<Session> allSessions = sessionRepository.findAll();
        List<Session> sessionsToReturn;

        UpdateFinishedSessions(allSessions);

        if ("COACH".equals(role) && authenticationService.hasRole("COACH")) {
            sessionsToReturn = allSessions.stream().filter(session -> session.getCoach().getId().equals(idFromDatabase)).collect(Collectors.toList());
        } else if ("COACHEE".equals(role) && authenticationService.hasRole("COACHEE")) {
            sessionsToReturn = allSessions.stream().filter(session -> session.getCoachee().getId().equals(idFromDatabase)).collect(Collectors.toList());
        } else if (authenticationService.hasRole("ADMIN")) {
            sessionsToReturn = allSessions;
        } else {
            throw new InvalidInputException("Role not recognised or invalid");
        }

        return sessionMapper.toDto(sessionsToReturn);
    }

    private void UpdateFinishedSessions(List<Session> allSessions) {
        UpdateFinishedAcceptedSessions(allSessions);
        UpdateFinishedRequestedSessions(allSessions);
    }

    private void UpdateFinishedAcceptedSessions(List<Session> allSessions) {
        Status statusDone = statusRepository.findById(STATUS_DONE_WAITING_FOR_FEEDBACK)
                .orElseThrow(() -> new DatabaseException(STATUS_DONE_WAITING_FOR_FEEDBACK + " not found in the database."));

        allSessions.stream()
                .filter(session -> STATUS_ACCEPTED.equals(session.getStatus().getStatusName()))
                .filter(session -> LocalDateTime.of(session.getDate(), session.getTime()).isBefore(LocalDateTime.now()))
                .forEach(session -> session.setStatus(statusDone));
    }

    private void UpdateFinishedRequestedSessions(List<Session> allSessions) {
        Status statusFinishedAutomaticaly = statusRepository.findById(STATUS_FINISHED_AUTOMATICALLY_CLOSED)
                .orElseThrow(() -> new DatabaseException(STATUS_FINISHED_AUTOMATICALLY_CLOSED + " not found in database."));

        allSessions.stream()
                .filter(session -> STATUS_REQUESTED.equals(session.getStatus().getStatusName()))
                .filter(session -> LocalDateTime.of(session.getDate(), session.getTime()).isBefore(LocalDateTime.now()))
                .forEach(session -> session.setStatus(statusFinishedAutomaticaly));
    }

    public void updateSessionStatus(String sessionId, String newStatus) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new InvalidInputException("Session Not found"));

        String coachId = session.getCoach().getId();
        String coacheeId = session.getCoachee().getId();

        Status status = statusRepository.findById(newStatus)
                .orElseThrow(() -> new InvalidInputException("Status not found"));

        if (authenticationService.hasRole("COACH") && coachId.equals(authenticationService.getAuthenticationIdFromDb())) {

            if (session.getStatus().getStatusName().equals(STATUS_REQUESTED)
                    && (newStatus.equals(STATUS_ACCEPTED) || newStatus.equals(STATUS_DECLINED))) {
                session.setStatus(status);
            } else if (session.getStatus().getStatusName().equals(STATUS_ACCEPTED)
                    && newStatus.equals(STATUS_FINISHED_CANCELLED_BY_COACH)) {
                session.setStatus(status);
                if (session.getCoachee().getTelephoneNumber() != null) {
                    smsSender.sendMessage(session.getCoachee().getTelephoneNumber(), "Your " + session.getSubject() + " session on " + session.getDate() + " at " + session.getTime() + " has been cancelled by the coach");
                }
            } else {
                throw new IllegalArgumentException("Status " + session.getStatus().getStatusName() + " can not be changed to " + newStatus + ".");
            }
        } else if (authenticationService.hasRole("COACHEE") && coacheeId.equals(authenticationService.getAuthenticationIdFromDb())) {

            if ((session.getStatus().getStatusName().equals(STATUS_REQUESTED) || (session.getStatus().getStatusName().equals(STATUS_ACCEPTED)))
                    && newStatus.equals(STATUS_FINISHED_CANCELLED_BY_COACHEE)) {
                session.setStatus(status);
            } else {
                throw new IllegalArgumentException("Status " + session.getStatus().getStatusName() + " can not be changed to " + newStatus + ".");
            }
        }
    }

    public void giveFeedback(String sessionId, FeedbackDto feedbackDto) {


        if (feedbackDto.getScore1() <= 0 || feedbackDto.getScore1() > 7 || feedbackDto.getScore2() <= 0 || feedbackDto.getScore2() > 7) {
            throw new InvalidInputException("Score has to be in a range between 1 and 7");
        }

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new InvalidInputException("Session Not found"));

        if (session.getStatus().getStatusName().contains(STATUS_FINISHED)) {
            throw new IllegalArgumentException("Session has been archived. Cannot add feedback anymore. ");
        }

        if (!session.getStatus().getStatusName().equals(STATUS_DONE_WAITING_FOR_FEEDBACK)) {
            throw new IllegalArgumentException("You cannot give feedback before a session is finished.");
        }

        String coachId = session.getCoach().getId();
        String coacheeId = session.getCoachee().getId();

        User feedbackGiver = userRepository.findById(authenticationService.getAuthenticationIdFromDb())
                .orElseThrow();
        Feedback feedback = feedbackMapper.toEntity(feedbackDto, feedbackGiver);

        if (authenticationService.hasRole("COACH")
                && coachId.equals(feedbackGiver.getId())) {
            if (session.getCoachFeedback() != null) {
                throw new FeedbackAlreadyProvidedException("Coach feedback has already been provided");
            }
            feedback = feedbackRepository.save(feedback);
            session.setCoachFeedback(feedback);
        } else if (authenticationService.hasRole("COACHEE")
                && coacheeId.equals(feedbackGiver.getId())) {
            if (session.getCoacheeFeedback() != null) {
                throw new FeedbackAlreadyProvidedException("Coachee feedback has already been provided");
            }
            feedback = feedbackRepository.save(feedback);
            session.setCoacheeFeedback(feedback);
        } else {
            throw new ForbiddenAccessException("You cannot give feedback for someone else's session");
        }

        if (session.getCoachFeedback() != null && session.getCoacheeFeedback() != null) {
            session.setStatus(statusRepository.getById(STATUS_FINISHED_FEEDBACK_GIVEN));

            User coach = userRepository.findById(coachId).orElseThrow();
            int previousXp = coach.getCoachInformation().getCoachXp();
            coach.getCoachInformation().setCoachXp(previousXp + 10);
        }
    }
}

