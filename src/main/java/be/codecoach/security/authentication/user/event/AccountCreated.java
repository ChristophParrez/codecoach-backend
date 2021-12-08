package be.codecoach.security.authentication.user.event;

import be.codecoach.security.authentication.user.accountverification.AccountVerification;
import be.codecoach.security.authentication.user.api.Account;

public class AccountCreated {

    private final Account account;
    private final AccountVerification accountVerification;

    public AccountCreated(Account account, AccountVerification accountVerification) {
        this.account = account;
        this.accountVerification = accountVerification;
    }

    public Account getAccount() {
        return account;
    }

    public Long getProfileId() {
        return accountVerification.getId();
    }

    public String getVerificationCode() {
        return accountVerification.getVerificationCode();
    }
}
