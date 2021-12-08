package be.codecoach.security.authentication;

public enum Authority implements GrantedAuthority {
    COACHEE("COACHEE"),
    COACH("COACH"),
    ADMIN("ADMIN");

    private final String authority;

    Authority(String authority) {
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
