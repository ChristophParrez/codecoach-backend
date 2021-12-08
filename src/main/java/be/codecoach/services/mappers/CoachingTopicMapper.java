package be.codecoach.services.mappers;

import be.codecoach.api.dtos.CoachingTopicDto;
import be.codecoach.domain.CoachingTopic;
import be.codecoach.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CoachingTopicMapper {

    private TopicMapper topicMapper;

    @Autowired
    public CoachingTopicMapper(TopicMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    public CoachingTopic toEntity(CoachingTopicDto dto) {
        Topic topic = topicMapper.toEntity(dto.getTopic());
        return new CoachingTopic(topic, dto.getExperience());
    }

    public List<CoachingTopic> toEntity(List<CoachingTopicDto> dto) {
        return dto.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
