package be.codecoach.exceptions;

public class FeedbackAlreadyProvidedException extends RuntimeException {
    public FeedbackAlreadyProvidedException(String message) {
        super(message);
    }
}
