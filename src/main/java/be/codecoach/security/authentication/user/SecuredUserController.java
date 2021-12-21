package be.codecoach.security.authentication.user;

import be.codecoach.security.authentication.user.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/security/account")
@CrossOrigin
public class SecuredUserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(SecuredUserController.class);

    private final SecuredUserService securedUserService;

    public SecuredUserController(SecuredUserService securedUserService) {
        this.securedUserService = securedUserService;
    }
/*
    @PreAuthorize("isAnonymous()")
    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8", path = "/validate")
    public VerificationResultDto validateAccount(@RequestBody ValidateAccountDto validationData) {
        return securedUserService.validateAccount(validationData);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8", path = "/resend-validation")
    public void resendValidation(@RequestBody ResendVerificationDto validationData) {
        securedUserService.resendValidation(validationData);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(consumes = "application/json;charset=UTF-8", path = "/password/request-reset")
    public void requestPasswordResetToken(@RequestBody PasswordResetRequestDto resetRequest) {
        securedUserService.requestPasswordReset(resetRequest);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8", path = "/password")
    public PasswordChangeResultDto performPasswordChange(@RequestBody PasswordChangeRequestDto changeRequest) {
        if (!isPasswordValid(changeRequest.getPassword())) {
            return new PasswordChangeResultDto(false);
        }
        return securedUserService.performPasswordChange(changeRequest);
    }*/
/*
    boolean isPasswordValid(String password) {
        if (password == null) return false;

        String pattern = "(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{8,}";
        return password.matches(pattern);
    }
    */

}
