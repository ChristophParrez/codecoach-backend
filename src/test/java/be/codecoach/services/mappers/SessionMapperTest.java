package be.codecoach.services.mappers;

import be.codecoach.api.dtos.LocationDto;
import be.codecoach.api.dtos.SessionDto;
import be.codecoach.domain.Location;
import be.codecoach.domain.Session;
import be.codecoach.domain.Status;
import be.codecoach.domain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SessionMapperTest {

    @Test
    void toEntity() {
        //given
        LocationDto locationDto = LocationDto.Builder.aLocationDto()
                .withName("Online")
                .build();

        String coacheeId = "coacheeUUID";
        String coachId = "coachUUID";
        String subject = "Java";
        LocalDate date = LocalDate.of(2022, 2, 23);
        LocalTime time = LocalTime.NOON;
        String remarks = "I would like some help with unit testing.";

        SessionDto sessionDto = SessionDto.Builder.aSessionDto()
                .withCoacheeId(coacheeId)
                .withCoachId(coachId)
                .withSubject(subject)
                .withDate(date)
                .withTime(time)
                .withLocation(locationDto)
                .withRemarks(remarks)
                .build();

        User coach = User.UserBuilder.user()
                .withId(coachId)
                .build();

        User coachee = User.UserBuilder.user()
                .withId(coacheeId)
                .build();

        Status status = new Status("REQUESTED");
        Location location = new Location(locationDto.getName());

        SessionMapper sessionMapper = new SessionMapper(new LocationMapper(), new StatusMapper(), new FeedbackMapper());

        //when
        Session session = sessionMapper.toEntity(sessionDto, coach, coachee, status, location);

        //then
        assertEquals("coacheeUUID", session.getCoachee().getId());
        assertEquals("coachUUID", session.getCoach().getId());
        assertEquals(subject, session.getSubject());
        assertEquals(date, session.getDate());
        assertEquals(time, session.getTime());
        assertEquals(location, session.getLocation());
        assertEquals(remarks, session.getRemarks());
        assertEquals(status, session.getStatus());
        assertNull(session.getCoacheeFeedback());
        assertNull(session.getCoachFeedback());


    }
}