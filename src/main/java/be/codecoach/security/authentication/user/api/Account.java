package be.codecoach.security.authentication.user.api;

import be.codecoach.domain.RoleEnum;

import java.util.List;

public interface Account {
    String getId();

    String getEmail();

    String getPassword();

    void setPassword(String encode);

    List<RoleEnum> getAuthorities();

    boolean isAccountEnabled();

    void enableAccount();
}
