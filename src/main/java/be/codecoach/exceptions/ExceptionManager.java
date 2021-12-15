package be.codecoach.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionManager {

    private final Logger logger = LoggerFactory.getLogger(ExceptionManager.class);

    @ExceptionHandler(NoInputException.class)
    protected void noInputException(NoInputException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(InvalidEmailException.class)
    protected void invalidEmailException(NoInputException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    protected void invalidPasswordException(InvalidPasswordException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(NotUniqueException.class)
    protected void notUniqueException(NotUniqueException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    protected void runtimeException(RuntimeException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    protected void internalAuthenticationServiceException(InternalAuthenticationServiceException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    protected void nullPointerException(NullPointerException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected void userNotFoundException(UserNotFoundException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    protected void forbiddenAccessException(ForbiddenAccessException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.FORBIDDEN.value(), exception.getMessage());
    }

    @ExceptionHandler(TopicException.class)
    protected void topicException(TopicException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(CoachingTopicException.class)
    protected void topicException(CoachingTopicException exception, HttpServletResponse response) throws Exception {
        logger.error(exception.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    // @Override
    // protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    //                                                               HttpHeaders headers,
    //                                                               HttpStatus status, WebRequest request) {
    //     Map<String, Object> body = new LinkedHashMap<>();
    //     body.put("timestamp", new Date());
    //     body.put("status", status.value());
    //
    //     List<String> errors = ex.getBindingResult()
    //             .getFieldErrors()
    //             .stream()
    //             .map(DefaultMessageSourceResolvable::getDefaultMessage)
    //             .collect(Collectors.toList());
    //
    //     body.put("errors", errors);
    //     return new ResponseEntity<>(body, headers, status);
    // }



}
