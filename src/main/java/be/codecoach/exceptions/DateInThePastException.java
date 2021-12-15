package be.codecoach.exceptions;

public class DateInThePastException extends RuntimeException {
    public DateInThePastException(String message) {
        super(message);
    }
}
