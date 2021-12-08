package be.codecoach.services.mappers;

import be.codecoach.api.dtos.TopicDto;
import be.codecoach.domain.Topic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopicMapperTest {

    @Test
    void givenTopicDto_WhenMapToEntity_ThenExpectCorrectEntity() {
        //given
        String topicName = "Biology";
        TopicDto topicDto = TopicDto.Builder.aTopicDto()
                .withName(topicName)
                .build();
        TopicMapper mapper = new TopicMapper();

        //when
        Topic actualTopic = mapper.toEntity(topicDto);

        //then
        Topic expectedTopic = new Topic("Biology");
        assertEquals(expectedTopic.getName(), actualTopic.getName());
    }
}