package be.codecoach.domain;

import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
    COACHEE("COACHEE"),
    COACH("COACH"),
    ADMIN("ADMIN");

    private final String authority;

    RoleEnum(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return authority;
    }
}
