package be.codecoach.security.authentication.user;

import be.codecoach.domain.RoleEnum;
import be.codecoach.security.authentication.user.api.Account;
import be.codecoach.security.authentication.user.api.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;


@Service
@Transactional
public class SecuredUserService implements UserDetailsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SecuredUserService.class);

    private final AccountService accountService;

    public SecuredUserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        LOGGER.info("loadUserByUsername()");
        Account account = accountService.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        Collection<RoleEnum> authorities = determineGrantedAuthorities(account);
        LOGGER.info("loadUserByUsername(): " + authorities);
        SecuredUser toPass = new SecuredUser(account.getEmail(), account.getPassword(), authorities, account.isAccountEnabled());
        LOGGER.info(String.valueOf(toPass));

        return toPass;
    }

    private Collection<RoleEnum> determineGrantedAuthorities(Account account) {
        return new ArrayList<>(account.getAuthorities());
    }
}
