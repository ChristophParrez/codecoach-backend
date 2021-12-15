package be.codecoach.services.validators;

import be.codecoach.api.dtos.SessionDto;
import be.codecoach.exceptions.NoInputException;
import be.codecoach.exceptions.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
public class SessionValidator {
    public void validate(SessionDto sessionDto) {
        assertObjectNotNull(sessionDto);
        assertFieldsNotNull(sessionDto);
        assertFieldsNull(sessionDto);
    }

    private void assertFieldsNull(SessionDto sessionDto) {
        if (sessionDto.getCoacheeFeedback() != null) {
            throw new UnexpectedInputException("Coachee feedback cannot be given before session takes place");
        }
        if (sessionDto.getCoachFeedback() != null) {
            throw new UnexpectedInputException("Coach feedback cannot be given before session takes place");
        }

    }


    public void assertFieldsNotNull(SessionDto sessionDto) {
        if (inputEmpty(sessionDto.getCoacheeId())) {
            throw new NoInputException("Coachee must be provided");
        }
        if (inputEmpty(sessionDto.getCoachId())) {
            throw new NoInputException("Coach must be provided");
        }
        if (inputEmpty(sessionDto.getSubject())) {
            throw new NoInputException("Subject must be provided");
        }
        if (inputEmpty(sessionDto.getDate().toString())) {
            throw new NoInputException("Date must be provided");
        }
        if (inputEmpty(sessionDto.getTime().toString())) {
            throw new NoInputException("Time must be provided");
        }
        if (inputEmpty(sessionDto.getLocation().toString())) {
            throw new NoInputException("Location must be provided");
        }

    }

    private boolean inputEmpty(String input) {
        return (input == null || input.isEmpty() || input.isBlank());
    }

    private void assertObjectNotNull(SessionDto sessionDto) {
        if (sessionDto.getDate() == null) {
            throw new NoInputException("Date must be provided");
        }
        if (sessionDto.getLocation() == null) {
            throw new NoInputException("Location must be provided");
        }
        if (sessionDto.getTime() == null) {
            throw new NoInputException("Time must be provided");
        }

    }

}
