package be.codecoach.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionManager {

    private final Logger logger = LoggerFactory.getLogger(ExceptionManager.class);

    @ExceptionHandler(NoInputException.class)
    protected void noInputException(NoInputException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(exception.getStatus().value(), exception.getMessage());
    }

    @ExceptionHandler(InvalidEmailException.class)
    protected void invalidEmailException(NoInputException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(exception.getStatus().value(), exception.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    protected void invalidPasswordException(InvalidPasswordException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(exception.getStatus().value(), exception.getMessage());
    }

    @ExceptionHandler(NotUniqueException.class)
    protected void notUniqueException(NotUniqueException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(exception.getStatus().value(), exception.getMessage());
    }



}
