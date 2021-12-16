package be.codecoach.services;

import be.codecoach.api.dtos.SessionDto;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.Session;
import be.codecoach.domain.Status;
import be.codecoach.domain.User;
import be.codecoach.exceptions.InvalidInputException;
import be.codecoach.exceptions.UserNotFoundException;
import be.codecoach.exceptions.WrongRoleException;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.SessionRepository;
import be.codecoach.repositories.StatusRepository;
import be.codecoach.repositories.UserRepository;
import be.codecoach.services.mappers.SessionMapper;
import be.codecoach.services.validators.SessionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    @Autowired
    public SessionService(UserRepository userRepository, RoleRepository roleRepository, SessionRepository sessionRepository, SessionValidator sessionValidator, SessionMapper sessionMapper, StatusRepository statusRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sessionRepository = sessionRepository;
        this.sessionValidator = sessionValidator;
        this.sessionMapper = sessionMapper;
        this.statusRepository = statusRepository;
        this.authenticationService = authenticationService;
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
        Session sessionToBeSaved = sessionMapper.toEntity(sessionDto, coachInDatabase.get(), coacheeInDatabase.get(), status);
        sessionRepository.save(sessionToBeSaved);
    }

    public List<SessionDto> getSessions(String role) {
        String idFromDatabase = authenticationService.getAuthenticationIdFromDb();

        List<Session> allSessions = sessionRepository.findAll();
        List<Session> sessionsToReturn;

        if (role.equals("COACH") && authenticationService.hasRole("COACH")) {
            sessionsToReturn = allSessions.stream().filter(session -> session.getCoach().getId().equals(idFromDatabase)).collect(Collectors.toList());
        } else if (role.equals("COACHEE") && authenticationService.hasRole("COACHEE")) {
            sessionsToReturn = allSessions.stream().filter(session -> session.getCoachee().getId().equals(idFromDatabase)).collect(Collectors.toList());
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

            // authenticationService.assertUserIsChangingOwnProfile(coachId, FORBIDDEN_ACCESS_MESSAGE);

            if (session.getStatus().getStatusName().equals("REQUESTED")
                    && (newStatus.equals("ACCEPTED") || newStatus.equals("DECLINED"))) {
                session.setStatus(status);
            } else {
                throw new IllegalArgumentException("Status " + session.getStatus().getStatusName() + " can not be changed to " + newStatus + ".");
            }
        }

        if (authenticationService.hasRole("COACHEE") && coacheeId.equals(authenticationService.getAuthenticationIdFromDb())) {

            // authenticationService.assertUserIsChangingOwnProfile(coacheeId, FORBIDDEN_ACCESS_MESSAGE);

            if ((session.getStatus().getStatusName().equals("REQUESTED") || (session.getStatus().getStatusName().equals("ACCEPTED")))
                    && newStatus.equals("FINISHED CANCELLED BY COACHEE")) {
                session.setStatus(status);
            } else {
                throw new IllegalArgumentException("Status " + session.getStatus().getStatusName() + " can not be changed to " + newStatus + ".");
            }
        }
    }

    private String checkIfCoachOrCoachee(String coachId, String coacheeId) {
        if (authenticationService.getAuthenticationIdFromDb().equals(coachId)) {
            return coachId;
        } else {
            return coacheeId;
        }
    }
}

