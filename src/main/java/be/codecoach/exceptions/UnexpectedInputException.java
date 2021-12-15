package be.codecoach.exceptions;

public class UnexpectedInputException extends RuntimeException {
    public UnexpectedInputException(String message) {
        super(message);
    }
}
