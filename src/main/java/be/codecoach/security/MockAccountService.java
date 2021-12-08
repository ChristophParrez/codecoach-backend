package be.codecoach.security;

import be.codecoach.security.authentication.user.api.AccountService;
import be.codecoach.security.authentication.user.api.CreateSecuredUserDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MockAccountService implements AccountService {

    @Override
    public Optional<MockAccount> findByEmail(String userName) {
        return Optional.of(new MockAccount());
    }

    @Override
    public MockAccount createAccount(CreateSecuredUserDto createSecuredUserDto) {
        return new MockAccount();
    }

    @Override
    public boolean existsByEmail(String email) {
        return true;
    }
}
