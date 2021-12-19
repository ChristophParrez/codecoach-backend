package be.codecoach.services.validators;

import be.codecoach.api.dtos.FeedbackDto;
import be.codecoach.api.dtos.LocationDto;
import be.codecoach.api.dtos.SessionDto;
import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.User;
import be.codecoach.exceptions.*;
import be.codecoach.repositories.LocationRepository;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionValidatorTest {

    LocationDto locationDto;
    SessionValidator sessionValidator;
    RoleRepository roleRepositoryMock;
    LocationRepository locationRepositoryMock;
    StatusRepository statusRepositoryMock;

    @BeforeEach
    void setUp() {
        locationDto = LocationDto.Builder.aLocationDto()
                .withName("Online")
                .build();

        roleRepositoryMock = Mockito.mock(RoleRepository.class);
        statusRepositoryMock = Mockito.mock(StatusRepository.class);
        locationRepositoryMock = Mockito.mock(LocationRepository.class);
        sessionValidator = new SessionValidator(roleRepositoryMock, locationRepositoryMock, statusRepositoryMock);
    }

    @Nested
    @DisplayName("Raw input validation")
    class ValidateRawInput {


        @Test
        @DisplayName("Expect no exceptions with a valid SessionDto")
        void givenValidSessionDto_whenCallingValidateSessionDto_thenExpectNoExceptions() {
            //given
            SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                    .withCoacheeId("coacheeUUID")
                    .withCoachId("coachUUID")
                    .withSubject("Java")
                    .withDate(LocalDate.of(2022, 2, 23))
                    .withTime(LocalTime.NOON)
                    .withLocation(locationDto)
                    .withRemarks("I would like some help with unit testing.")
                    .build();


            //when
            //then
            assertDoesNotThrow(() -> sessionValidator.validateRawInput(sessionDto));
        }

        @Nested
        @DisplayName("AssertObjectNotNull assertions:")
        class AssertObjectNotNull {

            @Test
            @DisplayName("SessionDto without a Date throws an exception")
            void givenSessionDtoWithoutDate_whenCallingValidateSessionDto_thenExpectExceptionThrown() {
                //given
                SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                        .withCoacheeId("coacheeUUID")
                        .withCoachId("coachUUID")
                        .withSubject("Java")
                        .withTime(LocalTime.NOON)
                        .withLocation(locationDto)
                        .withRemarks("I would like some help with unit testing.")
                        .build();
                //when
                //then
                assertThrows(NoInputException.class, () -> sessionValidator.validateRawInput(sessionDto));
            }

            @Test
            @DisplayName("SessionDto without a Time throws an exception")
            void givenSessionDtoWithoutTime_whenCallingValidateSessionDto_thenExpectExceptionThrown() {
                //given
                SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                        .withCoacheeId("coacheeUUID")
                        .withCoachId("coachUUID")
                        .withSubject("Java")
                        .withDate(LocalDate.of(2022, 2, 23))
                        .withLocation(locationDto)
                        .withRemarks("I would like some help with unit testing.")
                        .build();
                //when
                //then
                assertThrows(NoInputException.class, () -> sessionValidator.validateRawInput(sessionDto));
            }

            @Test
            @DisplayName("SessionDto without a Location throws an exception")
            void givenSessionDtoWithoutLocation_whenCallingValidateSessionDto_thenExpectExceptionThrown() {
                //given
                SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                        .withCoacheeId("coacheeUUID")
                        .withCoachId("coachUUID")
                        .withSubject("Java")
                        .withDate(LocalDate.of(2022, 2, 23))
                        .withTime(LocalTime.NOON)
                        .withRemarks("I would like some help with unit testing.")
                        .build();
                //when
                //then
                assertThrows(NoInputException.class, () -> sessionValidator.validateRawInput(sessionDto));
            }
        }

        @Nested
        @DisplayName("AssertFieldsNotNull assertions:")
        class AssertFieldsNotNull {
            @Test
            @DisplayName("SessionDto without a Coachee ID throws an exception")
            void givenSessionDtoWithoutCoacheeId_whenCallingValidateSessionDto_thenExpectExceptionThrown() {
                //given
                SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                        .withCoachId("coachUUID")
                        .withSubject("Java")
                        .withDate(LocalDate.of(2022, 2, 23))
                        .withTime(LocalTime.NOON)
                        .withLocation(locationDto)
                        .withRemarks("I would like some help with unit testing.")
                        .build();
                //when
                //then
                assertThrows(NoInputException.class, () -> sessionValidator.validateRawInput(sessionDto));
            }

            @Test
            @DisplayName("SessionDto without a Coach ID throws an exception")
            void givenSessionDtoWithoutCoachId_whenCallingValidateSessionDto_thenExpectExceptionThrown() {
                //given
                SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                        .withCoacheeId("coacheeUUID")
                        .withSubject("Java")
                        .withDate(LocalDate.of(2022, 2, 23))
                        .withTime(LocalTime.NOON)
                        .withLocation(locationDto)
                        .withRemarks("I would like some help with unit testing.")
                        .build();
                //when
                //then
                assertThrows(NoInputException.class, () -> sessionValidator.validateRawInput(sessionDto));
            }

            @Test
            @DisplayName("SessionDto without a Subject throws an exception")
            void givenSessionDtoWithoutSubject_whenCallingValidateSessionDto_thenExpectExceptionThrown() {
                //given
                SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                        .withCoacheeId("coacheeUUID")
                        .withCoachId("coachUUID")
                        .withDate(LocalDate.of(2022, 2, 23))
                        .withTime(LocalTime.NOON)
                        .withLocation(locationDto)
                        .withRemarks("I would like some help with unit testing.")
                        .build();
                //when
                //then
                assertThrows(NoInputException.class, () -> sessionValidator.validateRawInput(sessionDto));
            }
        }

        @Nested
        @DisplayName("AssertFieldsNull assertions:")
        class AssertFieldsNull {

            FeedbackDto feedbackDto;

            @BeforeEach
            void setUp() {
                feedbackDto = FeedbackDto.Builder.aFeedbackDto()
                        .build();
            }

            @Test
            void givenSessionDtoWithCoacheeFeedback_whenCallingValidateSessionDto_thenExpectExceptionThrown() {
                //given
                SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                        .withCoacheeId("coacheeUUID")
                        .withCoachId("coachUUID")
                        .withSubject("Java")
                        .withDate(LocalDate.of(2022, 2, 23))
                        .withTime(LocalTime.NOON)
                        .withLocation(locationDto)
                        .withRemarks("I would like some help with unit testing.")
                        .withCoacheeFeedback(feedbackDto)
                        .build();
                //when
                //then
                assertThrows(UnexpectedInputException.class, () -> sessionValidator.validateRawInput(sessionDto));
            }

            @Test
            void givenSessionDtoWithCoachFeedback_whenCallingValidateSessionDto_thenExpectExceptionThrown() {
                //given
                FeedbackDto feedbackDto = FeedbackDto.Builder.aFeedbackDto()
                        .build();

                SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                        .withCoacheeId("coacheeUUID")
                        .withCoachId("coachUUID")
                        .withSubject("Java")
                        .withDate(LocalDate.of(2022, 2, 23))
                        .withTime(LocalTime.NOON)
                        .withLocation(locationDto)
                        .withRemarks("I would like some help with unit testing.")
                        .withCoachFeedback(feedbackDto)
                        .build();
                //when
                //then
                assertThrows(UnexpectedInputException.class, () -> sessionValidator.validateRawInput(sessionDto));
            }

        }

        @Nested
        @DisplayName("AssertDateIsInTheFuture assertions:")
        class AssertDateIsInTheFuture {

            @Test
            @DisplayName("Date in the past throws an exception")
            void givenSessionDtoWithDateInThePast_whenCallingValidateSessionDto_thenExpectExceptionThrown() {
                //given
                SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                        .withCoacheeId("coacheeUUID")
                        .withCoachId("coachUUID")
                        .withSubject("Java")
                        .withDate(LocalDate.of(2021, 2, 23))
                        .withTime(LocalTime.NOON)
                        .withLocation(locationDto)
                        .withRemarks("I would like some help with unit testing.")
                        .build();
                //when
                //then
                assertThrows(DateInThePastException.class, () -> sessionValidator.validateRawInput(sessionDto));
            }

            @Test
            @DisplayName("Today but Time in the past, throws an exception")
            void givenSessionDtoWithDateTodayButTimeInThePast_whenCallingValidateSessionDto_thenExpectExceptionThrown() {
                //given
                SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                        .withCoacheeId("coacheeUUID")
                        .withCoachId("coachUUID")
                        .withSubject("Java")
                        .withDate(LocalDate.now())
                        .withTime(LocalTime.now().minusMinutes(1))
                        .withLocation(locationDto)
                        .withRemarks("I would like some help with unit testing.")
                        .build();
                //when
                //then
                assertThrows(DateInThePastException.class, () -> sessionValidator.validateRawInput(sessionDto));
            }

        }

    }

    @Nested
    @DisplayName("Assert given id's are valid")
    class AssertGivenIdsAreValid {

        @Test
        @DisplayName("Coach does not exist in database")
        void givenCoachIdInSessionDtoDoesNotExist_whenAssertGivenIdsAreValid_thenThrowUserNotFoundException() {
            //given
            Optional<User> coachInDatabase = Optional.empty();
            Optional<User> coacheeInDatabase = Optional.of(new User());
            //when
            //then
            assertThrows(UserNotFoundException.class, () -> sessionValidator.assertGivenIdsAreValid(coachInDatabase, coacheeInDatabase));
        }

        @Test
        @DisplayName("Coachee does not exist in database")
        void givenCoacheeIdInSessionDtoDoesNotExist_whenAssertGivenIdsAreValid_thenThrowUserNotFoundException() {
            //given
            Optional<User> coachInDatabase = Optional.of(new User());
            Optional<User> coacheeInDatabase = Optional.empty();
            //when
            //then
            assertThrows(UserNotFoundException.class, () -> sessionValidator.assertGivenIdsAreValid(coachInDatabase, coacheeInDatabase));
        }

        @Test
        @DisplayName("id exists in database, but it's not a coach")
        void givenCoachIdInSessionDtoDoesNotExistAs_whenAssertGivenIdsAreValid_thenThrowWrongRoleException() {
            //given
            User notACoach = User.UserBuilder.user()
                    .withRoles(Set.of(Role.RoleBuilder.role()
                            .withRoleEnum(RoleEnum.COACHEE)
                            .build()))
                    .build();

            Mockito.when(roleRepositoryMock.findByRole(RoleEnum.COACH))
                    .thenReturn(Role.RoleBuilder.role()
                            .withRoleEnum(RoleEnum.COACH)
                            .build());

            Optional<User> coachInDatabase = Optional.of(notACoach);
            Optional<User> coacheeInDatabase = Optional.of(new User());

            //when
            //then
            assertThrows(WrongRoleException.class, () -> sessionValidator.assertGivenIdsAreValid(coachInDatabase, coacheeInDatabase));
        }

    }


}