package be.codecoach.services.mappers;

import be.codecoach.api.dtos.CoachingTopicDto;
import be.codecoach.domain.CoachingTopic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CoachingTopicMapper {

    public CoachingTopic toEntity(CoachingTopicDto dto) {
        return new CoachingTopic(dto.getTopic(), dto.getExperience());
    }

    public List<CoachingTopic> toEntity(List<CoachingTopicDto> dto) {
        return dto.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
