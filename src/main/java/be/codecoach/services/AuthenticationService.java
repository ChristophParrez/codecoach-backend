package be.codecoach.services;

import be.codecoach.exceptions.ForbiddenAccessException;
import be.codecoach.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void assertUserIsChangingOwnProfileOrIsAdmin(String userId, String message) {
        if (!userId.equals(getAuthenticationIdFromDb()) && !hasRole("ADMIN")) {
            throw new ForbiddenAccessException(message);
        }
    }

    public boolean hasRole(String roleName) {
        logger.info("User " + getEmailFromAuthentication() + " has roles " + getAuthentication().getAuthorities());
        return getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(roleName));
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getAuthenticationIdFromDb() {
        logger.info("Extracting Id from DB from email address " + getEmailFromAuthentication());
        return userRepository.findByEmail(getEmailFromAuthentication())
                .orElseThrow(() -> new NullPointerException("Email from token was not found in the database."))
                .getId();
    }

    public String getEmailFromAuthentication() {
        return getAuthentication().getName();
    }
}
