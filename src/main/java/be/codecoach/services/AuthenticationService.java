package be.codecoach.services;

import be.codecoach.exceptions.ForbiddenAccessException;
import be.codecoach.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void assertUserIsChangingOwnProfileOrIsAdmin(String userId, String message) {
        if (!userId.equals(getAuthenticationIdFromDb()) && !hasRole("ADMIN")) {
            throw new ForbiddenAccessException(message);
        }
    }

    public boolean hasRole(String roleName) {
        System.out.println("Authorities from hasRole(): " + getAuthentication().getAuthorities());
        return getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(roleName));
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getAuthenticationIdFromDb() {
                System.out.println("getAuthenticationIdFromDb() -> getAuthentication().getName() = " + getEmailFromAuthentication());
        return userRepository.findByEmail(getEmailFromAuthentication())
                .orElseThrow(() -> new NullPointerException("Email from token was not found in the database."))
                .getId();
    }

    public String getEmailFromAuthentication() {
        return getAuthentication().getName();
    }
}
