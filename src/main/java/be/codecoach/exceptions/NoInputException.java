package be.codecoach.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoInputException extends ResponseStatusException {

    public NoInputException(String message) {super(HttpStatus.BAD_REQUEST, message);}
}
