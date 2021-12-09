package be.codecoach.security.authentication.user.api;

import be.codecoach.api.dtos.UserDto;

import java.util.Optional;


public interface AccountService {
    Optional<? extends Account> findByEmail(String userName);

    Account createAccount(CreateSecuredUserDto createSecuredUserDto);

    boolean existsByEmail(String email);
}
