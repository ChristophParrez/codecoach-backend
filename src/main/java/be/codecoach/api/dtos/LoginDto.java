package be.codecoach.api.dtos;

public class LoginDto {

    private final String username;
    private final String password;

    private LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public static final class Builder {
        private String username;
        private String password;

        private Builder() {
        }

        public static Builder aLoginDto() {
            return new Builder();
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public LoginDto build() {
            return new LoginDto(username, password);
        }
    }
}
