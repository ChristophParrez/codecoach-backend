package be.codecoach.security.authentication.user.api;

import be.codecoach.security.authentication.user.Authority;

import java.util.List;

public interface Account {
    Long getId();

    String getEmail();

    String getPassword();
    void setPassword(String encode);

    List<Authority> getAuthorities();

    boolean isAccountEnabled();
    void enableAccount();
}
