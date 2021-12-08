package be.codecoach.services.mappers;

import be.codecoach.api.dtos.CoachInformationDto;
import be.codecoach.domain.CoachInformation;
import be.codecoach.domain.CoachingTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoachInformationMapper {

    private CoachingTopicMapper coachingTopicMapper;

    @Autowired
    public CoachInformationMapper(CoachingTopicMapper coachingTopicMapper) {
        this.coachingTopicMapper = coachingTopicMapper;
    }

    public CoachInformation toEntity(CoachInformationDto dto) {
        List<CoachingTopic> coachingTopics = coachingTopicMapper.toEntity(dto.getCoachingTopics());
        return new CoachInformation(dto.getCoachXp(), dto.getIntroduction(), dto.getAvailability(), coachingTopics);
    }
}
