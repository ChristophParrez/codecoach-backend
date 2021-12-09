package be.codecoach.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoInputException extends RuntimeException {

    public NoInputException(String message) {super(message);}
}
