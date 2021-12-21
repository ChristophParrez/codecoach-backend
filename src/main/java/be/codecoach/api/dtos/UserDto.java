package be.codecoach.api.dtos;

import java.util.Set;

public class UserDto {

    private final String userId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String telephoneNumber;
    private final String companyName;
    private final String password;
    private final Set<RoleDto> roles;
    private final String picture;
    private final CoachInformationDto coachInformation;

    private UserDto(String userId, String firstName, String lastName, String email, String telephoneNumber, String companyName, String password, Set<RoleDto> roles, String picture, CoachInformationDto coachInformation) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.companyName = companyName;
        this.password = password;
        this.roles = roles;
        this.picture = picture;
        this.coachInformation = coachInformation;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPassword() {
        return password;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public String getPicture() {
        return picture;
    }

    public CoachInformationDto getCoachInformation() {
        return coachInformation;
    }

    public static final class Builder {
        private String userId;
        private String firstName;
        private String lastName;
        private String email;
        private String telephoneNumber;
        private String companyName;
        private String password;
        private Set<RoleDto> roles;
        private String picture;
        private CoachInformationDto coachInformation;

        private Builder() {
        }

        public static Builder anUserDto() {
            return new Builder();
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withTelephoneNumber(String telephoneNumber) {
            this.telephoneNumber = telephoneNumber;
            return this;
        }

        public Builder withCompanyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withRoles(Set<RoleDto> roles) {
            this.roles = roles;
            return this;
        }

        public Builder withPicture(String picture) {
            this.picture = picture;
            return this;
        }

        public Builder withCoachInformation(CoachInformationDto coachInformation) {
            this.coachInformation = coachInformation;
            return this;
        }

        public UserDto build() {
            return new UserDto(userId, firstName, lastName, email, telephoneNumber, companyName, password, roles, picture, coachInformation);
        }
    }
}
