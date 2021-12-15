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

    public void assertUserIsChangingOwnProfile(String userId) {
        if (!userId.equals(getAuthenticationIdFromDb())) {
            throw new ForbiddenAccessException("You cannot change someone else's profile!");
        }
    }

    public boolean hasRole(String roleName) {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(roleName));
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getAuthenticationIdFromDb() {
        return userRepository.findByEmail(getAuthentication().getName())
                .orElseThrow(() -> new NullPointerException("Email from token was not found in the database."))
                .getId();
    }
}
