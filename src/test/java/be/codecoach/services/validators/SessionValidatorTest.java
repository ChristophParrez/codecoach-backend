package be.codecoach.services.validators;

import be.codecoach.api.dtos.FeedbackDto;
import be.codecoach.api.dtos.LocationDto;
import be.codecoach.api.dtos.SessionDto;
import be.codecoach.exceptions.DateInThePastException;
import be.codecoach.exceptions.NoInputException;
import be.codecoach.exceptions.UnexpectedInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class SessionValidatorTest {

    LocationDto locationDto;
    SessionValidator sessionValidator;

    @BeforeEach
    void setUp() {
        locationDto = LocationDto.Builder.aLocationDto()
                .withName("Online")
                .build();

        sessionValidator = new SessionValidator();
    }

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
        assertDoesNotThrow(() -> sessionValidator.validate(sessionDto));
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
            assertThrows(NoInputException.class, () -> sessionValidator.validate(sessionDto));
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
            assertThrows(NoInputException.class, () -> sessionValidator.validate(sessionDto));
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
            assertThrows(NoInputException.class, () -> sessionValidator.validate(sessionDto));
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
            assertThrows(NoInputException.class, () -> sessionValidator.validate(sessionDto));
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
            assertThrows(NoInputException.class, () -> sessionValidator.validate(sessionDto));
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
            assertThrows(NoInputException.class, () -> sessionValidator.validate(sessionDto));
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
            assertThrows(UnexpectedInputException.class, () -> sessionValidator.validate(sessionDto));
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
            assertThrows(UnexpectedInputException.class, () -> sessionValidator.validate(sessionDto));
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
            assertThrows(DateInThePastException.class, () -> sessionValidator.validate(sessionDto));
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
            assertThrows(DateInThePastException.class, () -> sessionValidator.validate(sessionDto));
        }

    }


}