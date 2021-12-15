package be.codecoach.services.mappers;

import be.codecoach.api.dtos.FeedbackDto;
import be.codecoach.domain.Feedback;
import be.codecoach.domain.User;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {


    public Feedback toEntity(FeedbackDto dto, User feedbackGiver) {
        return new Feedback(feedbackGiver, dto.getScore1(), dto.getScore2(), dto.getComment());
    }

    public FeedbackDto toDto(Feedback feedback) {
        return FeedbackDto.Builder.aFeedbackDto()
                .withId(feedback.getId())
                .withFeedbackGiverId(feedback.getFeedbackGiver().getId())
                .withScore1(feedback.getScore1())
                .withScore2(feedback.getScore2())
                .withComment(feedback.getComment())
                .build();
    }
}
