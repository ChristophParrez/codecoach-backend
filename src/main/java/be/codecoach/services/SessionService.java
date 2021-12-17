package be.codecoach.services;

import be.codecoach.api.dtos.FeedbackDto;
import be.codecoach.api.dtos.SessionDto;
import be.codecoach.domain.*;
import be.codecoach.exceptions.*;
import be.codecoach.repositories.*;
import be.codecoach.services.mappers.FeedbackMapper;
import be.codecoach.services.mappers.SessionMapper;
import be.codecoach.services.validators.SessionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionService {

    private static final String FORBIDDEN_ACCESS_MESSAGE = "You cannot update sessions for somebody else";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SessionRepository sessionRepository;
    private final SessionValidator sessionValidator;
    private final SessionMapper sessionMapper;
    private final StatusRepository statusRepository;
    private final AuthenticationService authenticationService;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public SessionService(UserRepository userRepository, RoleRepository roleRepository, SessionRepository sessionRepository, SessionValidator sessionValidator, SessionMapper sessionMapper, StatusRepository statusRepository, AuthenticationService authenticationService, FeedbackMapper feedbackMapper, FeedbackRepository feedbackRepository, LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sessionRepository = sessionRepository;
        this.sessionValidator = sessionValidator;
        this.sessionMapper = sessionMapper;
        this.statusRepository = statusRepository;
        this.authenticationService = authenticationService;
        this.feedbackMapper = feedbackMapper;
        this.feedbackRepository = feedbackRepository;
        this.locationRepository = locationRepository;
    }

    public void requestSession(SessionDto sessionDto) {
        String userId = sessionDto.getCoacheeId();
        authenticationService.assertUserIsChangingOwnProfile(userId, FORBIDDEN_ACCESS_MESSAGE);

        Optional<User> coachInDatabase = userRepository.findById(sessionDto.getCoachId());
        Optional<User> coacheeInDatabase = userRepository.findById(sessionDto.getCoacheeId());

        if (coachInDatabase.isEmpty()) {
            throw new UserNotFoundException("No user was found with this coach id");
        }
        if (coacheeInDatabase.isEmpty()) {
            throw new UserNotFoundException("No user was found with this coachee id");
        }
        if (!coachInDatabase.get().getRoles().contains(roleRepository.findByRole(RoleEnum.COACH))) {
            throw new WrongRoleException("This user is not a coach. The user can't receive session requests");
        }

        assertSessionInfoIsValid(sessionDto);

        Status status = statusRepository.getById("REQUESTED");
        Location location = locationRepository.findById(sessionDto.getLocation().getName())
                .orElseThrow( () -> new InvalidInputException("This location is not available."));
        Session sessionToBeSaved = sessionMapper.toEntity(sessionDto, coachInDatabase.get(), coacheeInDatabase.get(), status, location);
        sessionRepository.save(sessionToBeSaved);
    }

    public List<SessionDto> getSessions(String role) {
        String idFromDatabase = authenticationService.getAuthenticationIdFromDb();

        List<Session> allSessions = sessionRepository.findAll();
        List<Session> sessionsToReturn;

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

    private void assertSessionInfoIsValid(SessionDto sessionDto) {
        sessionValidator.validate(sessionDto);
    }

    public void updateSessionStatus(String sessionId, String newStatus) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new InvalidInputException("Session Not found"));

        String coachId = session.getCoach().getId();
        String coacheeId = session.getCoachee().getId();

        newStatus = newStatus.replaceAll("_", " ");

        Status status = statusRepository.findById(newStatus)
                .orElseThrow(() -> new InvalidInputException("Status not found"));

        if (authenticationService.hasRole("COACH") && coachId.equals(authenticationService.getAuthenticationIdFromDb())) {

            if (session.getStatus().getStatusName().equals("REQUESTED")
                    && (newStatus.equals("ACCEPTED") || newStatus.equals("DECLINED"))) {
                session.setStatus(status);
            } else if (session.getStatus().getStatusName().equals("ACCEPTED")
                    && newStatus.equals("FINISHED CANCELLED BY COACH")) {
                session.setStatus(status);
            } else {
                throw new IllegalArgumentException("Status " + session.getStatus().getStatusName() + " can not be changed to " + newStatus + ".");
            }
        } else if (authenticationService.hasRole("COACHEE") && coacheeId.equals(authenticationService.getAuthenticationIdFromDb())) {

            if ((session.getStatus().getStatusName().equals("REQUESTED") || (session.getStatus().getStatusName().equals("ACCEPTED")))
                    && newStatus.equals("FINISHED CANCELLED BY COACHEE")) {
                session.setStatus(status);
            } else {
                throw new IllegalArgumentException("Status " + session.getStatus().getStatusName() + " can not be changed to " + newStatus + ".");
            }
        }
    }

    public void giveFeedback(String sessionId, FeedbackDto feedbackDto) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new InvalidInputException("Session Not found"));

        if (session.getStatus().getStatusName().contains("FINISHED")) {
            throw new IllegalArgumentException("Session has been archived. Cannot add feedback anymore. ");
        }

        if (!session.getStatus().getStatusName().equals("DONE WAITING FOR FEEDBACK")) {
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
            session.setStatus(statusRepository.getById("FINISHED FEEDBACK GIVEN"));

            User coach = userRepository.findById(coachId).orElseThrow();
            int previousXp = coach.getCoachInformation().getCoachXp();
            coach.getCoachInformation().setCoachXp(previousXp+10);
        }
    }
}

