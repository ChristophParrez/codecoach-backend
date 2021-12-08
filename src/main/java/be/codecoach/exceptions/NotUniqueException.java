package be.codecoach.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotUniqueException extends ResponseStatusException {
    public NotUniqueException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
