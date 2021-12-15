package be.codecoach.services.mappers;

import be.codecoach.api.dtos.SessionDto;
import be.codecoach.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionMapper {

    private final LocationMapper locationMapper;
    private final StatusMapper statusMapper;
    private final FeedbackMapper feedbackMapper;

    @Autowired
    public SessionMapper(LocationMapper locationMapper, StatusMapper statusMapper, FeedbackMapper feedbackMapper) {
        this.locationMapper = locationMapper;
        this.statusMapper = statusMapper;
        this.feedbackMapper = feedbackMapper;
    }

    public Session toEntity(SessionDto dto, User coach, User coachee, Status status) {
        Location location = locationMapper.toEntity(dto.getLocation());
        Feedback coachFeedback = dto.getCoachFeedback() != null ? feedbackMapper.toEntity(dto.getCoachFeedback(), coach) : null;
        Feedback coacheeFeedback = dto.getCoacheeFeedback() != null ? feedbackMapper.toEntity(dto.getCoacheeFeedback(), coachee) : null;

        return new Session(coach, coachee, dto.getSubject(), dto.getDate(), dto.getTime(), location, dto.getRemarks(), status, coachFeedback, coacheeFeedback);
    }

    public SessionDto toDto(Session session) {
        return SessionDto.Builder.aSessionDto()
                .withCoachId(session.getCoach().getId())
                .withCoacheeId(session.getCoachee().getId())
                .withSubject(session.getSubject())
                .withDate(session.getDate())
                .withTime(session.getTime())
                .withLocation(locationMapper.toDto(session.getLocation()))
                .withRemarks(session.getRemarks())
                .withStatus(statusMapper.toDto(session.getStatus()))
                .withCoachFeedback(session.getCoachFeedback() == null ? null : feedbackMapper.toDto(session.getCoachFeedback()))
                .withCoacheeFeedback(session.getCoacheeFeedback() == null ? null : feedbackMapper.toDto(session.getCoacheeFeedback()))
                .build();
    }

    public List<SessionDto> toDto(List<Session> sessions) {
        return sessions.stream().map(this::toDto).collect(Collectors.toList());
    }
}
