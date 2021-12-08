package be.codecoach.security.authorization;

import be.codecoach.security.authentication.user.api.Account;
import be.codecoach.security.authentication.user.api.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static be.codecoach.security.authentication.user.Authority.ADMIN;

@Service
public class AuthorizationService {

    private final AccountService accountService;

    public AuthorizationService(AccountService accountService) {
        this.accountService = accountService;
    }

    public boolean canAccessProfile(Authentication authentication, long profileIdentifier) {
        return authentication.getAuthorities().contains(ADMIN) || accountService.findByEmail(authentication.getName()).map(Account::getId).map(id -> id.equals(profileIdentifier)).orElse(false);
    }

    public boolean canChangeRole(Authentication authentication) {
        return authentication.getAuthorities().contains(ADMIN);
    }

    public boolean canAccessSession(Authentication authentication, long profileIdentifier) {
        return canAccessProfile(authentication, profileIdentifier);
    }
}
