package be.codecoach.services.validators;

import be.codecoach.api.dtos.SessionDto;
import be.codecoach.domain.Location;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.Status;
import be.codecoach.domain.User;
import be.codecoach.exceptions.*;
import be.codecoach.repositories.LocationRepository;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.StatusRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Component
public class SessionValidator {

    private final RoleRepository roleRepository;
    private final LocationRepository locationRepository;
    private final StatusRepository statusRepository;

    public SessionValidator(RoleRepository roleRepository, LocationRepository locationRepository, StatusRepository statusRepository) {
        this.roleRepository = roleRepository;
        this.locationRepository = locationRepository;
        this.statusRepository = statusRepository;
    }

    public void validateRawInput(SessionDto sessionDto) {
        assertObjectNotNull(sessionDto);
        assertFieldsNotNull(sessionDto);
        assertFieldsNull(sessionDto);
        assertDateIsInTheFuture(sessionDto);
    }

    public void assertGivenIdsAreValid(Optional<User> coachInDatabase, Optional<User> coacheeInDatabase){
        if (coachInDatabase.isEmpty()) {
            throw new UserNotFoundException("No user was found with this coach id");
        }
        if (coacheeInDatabase.isEmpty()) {
            throw new UserNotFoundException("No user was found with this coachee id");
        }
        if (!coachInDatabase.get().getRoles().contains(roleRepository.findByRole(RoleEnum.COACH))) {
            throw new WrongRoleException("This user is not a coach. The user can't receive session requests");
        }
    }

    public Location validateLocationMatchesGivenPossibilities(String location) {
        return locationRepository.findById(location)
                .orElseThrow( () -> new InvalidInputException("This location is not available."));
    }

    private void assertFieldsNull(SessionDto sessionDto) {
        if (sessionDto.getCoacheeFeedback() != null) {
            throw new UnexpectedInputException("Coachee feedback cannot be given before session takes place");
        }
        if (sessionDto.getCoachFeedback() != null) {
            throw new UnexpectedInputException("Coach feedback cannot be given before session takes place");
        }
    }

    private void assertFieldsNotNull(SessionDto sessionDto) {
        if (inputEmpty(sessionDto.getCoacheeId())) {
            throw new NoInputException("Coachee must be provided");
        }
        if (inputEmpty(sessionDto.getCoachId())) {
            throw new NoInputException("Coach must be provided");
        }
        if (inputEmpty(sessionDto.getSubject())) {
            throw new NoInputException("Subject must be provided");
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

    private void assertDateIsInTheFuture(SessionDto sessionDto) {
        LocalDate datePart = sessionDto.getDate();
        LocalTime timePart = sessionDto.getTime();
        LocalDateTime dateTime = LocalDateTime.of(datePart, timePart);

        if (dateTime.isBefore(LocalDateTime.now())) {
            String message = "The requested date and time must be in the future. Requested date/time: " + dateTime + ". Now: " + LocalDateTime.now();
            throw new DateInThePastException(message);
        }
    }


    public Status getStatusFromRepository(String status) {
        return statusRepository.findById("REQUESTED")
                .orElseThrow( () -> new DatabaseException("Database seems to have been modified! Was trying to fetch REQUESTED status from database in order to link it to the session, but REQUESTED was not found in the database."));
    }
}
