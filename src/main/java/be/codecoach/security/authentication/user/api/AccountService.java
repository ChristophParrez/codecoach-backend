package be.codecoach.security.authentication.user.api;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AccountService {
    Optional<? extends Account> findByEmail(String userName);

    Account createAccount(CreateSecuredUserDto createSecuredUserDto);

    boolean existsByEmail(String email);
}
