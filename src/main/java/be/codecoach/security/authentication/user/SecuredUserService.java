package be.codecoach.security.authentication.user;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.RoleEnum;
import be.codecoach.exceptions.UserNotFoundException;
import be.codecoach.security.authentication.user.accountverification.AccountVerificationService;
import be.codecoach.security.authentication.user.api.*;
import be.codecoach.security.authentication.user.password.reset.PasswordResetService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


@Service
@Transactional
public class SecuredUserService implements UserDetailsService {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    private final AccountVerificationService accountVerificationService;
    private final PasswordResetService passwordResetService;

    public SecuredUserService(AccountService accountService, AccountMapper accountMapper, PasswordEncoder passwordEncoder, AccountVerificationService accountVerificationService, PasswordResetService passwordResetService) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.accountVerificationService = accountVerificationService;
        this.passwordResetService = passwordResetService;
    }

    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername()");
        Account account = accountService.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        Collection<RoleEnum> authorities = determineGrantedAuthorities(account);
        System.out.println("loadUserByUsername(): " + authorities);
        SecuredUser toPass = new SecuredUser(account.getEmail(), account.getPassword(), authorities, account.isAccountEnabled());
        System.out.println(toPass);
        System.out.println(passwordEncoder.matches("String12!", account.getPassword()));
        System.out.println(Objects.equals(passwordEncoder.encode("String12!"), account.getPassword()));

        return toPass;

        //return new SecuredUser(account.getEmail(), account.getPassword(), authorities, account.isAccountEnabled());
    }

    private Collection<RoleEnum> determineGrantedAuthorities(Account account) {
        return new ArrayList<>(account.getAuthorities());
    }

    public SecuredUserDto registerAccount(UserDto userDto) {
        if (emailExists(userDto.getEmail())) {
            throw new IllegalStateException("Email already exists!");
        }
        Account account = accountService.createAccount(userDto);
        account.setPassword(passwordEncoder.encode(userDto.getPassword()));

        accountVerificationService.sendVerificationEmail(account);

        return accountMapper.toUserDto(account);
    }

    private boolean emailExists(String email) {
        return accountService.existsByEmail(email);
    }

    public VerificationResultDto validateAccount(ValidateAccountDto validationData) {
        Account account = accountService.findByEmail(validationData.getEmail()).orElseThrow(() -> new UserNotFoundException(""));
        return new VerificationResultDto(accountVerificationService.enableAccount(validationData.getVerificationCode(), account));
    }

    public void resendValidation(ResendVerificationDto validationData) {
        Account account = accountService.findByEmail(validationData.getEmail()).orElseThrow(() -> new RuntimeException("Account not found"));
        accountVerificationService.resendVerificationEmailFor(account);
    }

    public void requestPasswordReset(PasswordResetRequestDto resetRequest) {
        passwordResetService.requestPasswordReset(resetRequest);
    }

    public PasswordChangeResultDto performPasswordChange(PasswordChangeRequestDto changeRequest) {
        return passwordResetService.performPasswordChange(changeRequest);
    }
}
