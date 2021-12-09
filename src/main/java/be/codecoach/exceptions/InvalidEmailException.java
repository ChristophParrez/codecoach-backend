package be.codecoach.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException(String message) {super(message);}
}
