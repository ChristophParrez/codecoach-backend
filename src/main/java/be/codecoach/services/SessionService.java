package be.codecoach.services;

import be.codecoach.api.dtos.SessionDto;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.Session;
import be.codecoach.domain.Status;
import be.codecoach.domain.User;
import be.codecoach.exceptions.ForbiddenAccessException;
import be.codecoach.exceptions.UserNotFoundException;
import be.codecoach.exceptions.WrongRoleException;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.SessionRepository;
import be.codecoach.repositories.StatusRepository;
import be.codecoach.repositories.UserRepository;
import be.codecoach.services.mappers.SessionMapper;
import be.codecoach.services.validators.SessionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SessionRepository sessionRepository;
    private final SessionValidator sessionValidator;
    private final SessionMapper sessionMapper;
    private final StatusRepository statusRepository;

    @Autowired
    public SessionService(UserRepository userRepository, RoleRepository roleRepository, SessionRepository sessionRepository, SessionValidator sessionValidator, SessionMapper sessionMapper, StatusRepository statusRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sessionRepository = sessionRepository;
        this.sessionValidator = sessionValidator;
        this.sessionMapper = sessionMapper;
        this.statusRepository = statusRepository;
    }

    public Session requestSession(SessionDto sessionDto) {

        String userId = sessionDto.getCoacheeId();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authorities: " + authentication);

        boolean hasUserRoleCoachee = hasRole(authentication, "COACHEE");

        String emailFromToken = authentication.getName();
        String idFromDatabase = userRepository.findByEmail(emailFromToken).orElseThrow(() -> new NullPointerException("Email from token was not found in the database.")).getId();
        System.out.println("DB: " + idFromDatabase);
        System.out.println("userId: " +userId);
        if (hasUserRoleCoachee) {
            if (!userId.equals(idFromDatabase)) {
                throw new ForbiddenAccessException("You cannot request a session for somebody else.");
            }
        }

        Optional<User> coachInDatabase = userRepository.findById(sessionDto.getCoachId());
        Optional<User> coacheeInDatabase = userRepository.findById(sessionDto.getCoacheeId());
        if(coachInDatabase.isEmpty()) {
            throw new UserNotFoundException("No user was found with this coach id");
        }
        if(coacheeInDatabase.isEmpty()) {
            throw new UserNotFoundException("No user was found with this coachee id");
        }
        if(!coachInDatabase.get().getRoles().contains(roleRepository.findByRole(RoleEnum.COACH))){
            throw new WrongRoleException("This user is not a coach. The user can't receive session requests");
        }

        assertSessionInfoIsValid(sessionDto);

        Status status = statusRepository.getById("REQUESTED");
        Session sessionToBeSaved = sessionMapper.toEntity(sessionDto, coachInDatabase.get(), coacheeInDatabase.get(), status);
        return sessionRepository.save(sessionToBeSaved);
    }

        private boolean hasRole(Authentication authentication, String roleName) {
            return authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals(roleName));
        }

        private void assertSessionInfoIsValid(SessionDto sessionDto) {
            sessionValidator.validate(sessionDto);
        }

}
