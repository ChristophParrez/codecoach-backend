package be.codecoach.services.mappers;

import be.codecoach.api.dtos.TopicDto;
import be.codecoach.domain.Topic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class TopicMapper {

    public Topic toEntity(TopicDto dto) {
        return new Topic(dto.getName());
    }

    public TopicDto toDto(Topic topic) {
        return TopicDto.Builder.aTopicDto().withName(topic.getName()).build();
    }

    public List<TopicDto> toDto(List<Topic> topics) {
        return topics.stream().map(this::toDto).collect(Collectors.toList());
    }

}
