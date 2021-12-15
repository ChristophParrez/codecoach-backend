package be.codecoach.security.authentication.user.api;

public class VerificationResultDto {
    private boolean emailAddressValidated;

    public VerificationResultDto() {
    }

    public VerificationResultDto(boolean emailAddressValidated) {
        this.emailAddressValidated = emailAddressValidated;
    }

    public boolean isEmailAddressValidated() {
        return emailAddressValidated;
    }
}
