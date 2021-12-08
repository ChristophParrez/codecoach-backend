package be.codecoach.services.mappers;

import be.codecoach.api.dtos.CoachingTopicDto;
import be.codecoach.api.dtos.TopicDto;
import be.codecoach.domain.CoachingTopic;
import be.codecoach.domain.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoachingTopicMapperTest {

    TopicDto topicDto1;
    TopicDto topicDto2;
    CoachingTopicDto coachingTopicDto1;
    CoachingTopicDto coachingTopicDto2;
    CoachingTopic expectedCoachingTopic1;
    CoachingTopic expectedCoachingTopic2;
    TopicMapper topicMapper;
    CoachingTopicMapper coachingTopicMapper;
    Topic mappedTopic1;
    Topic mappedTopic2;


    @BeforeEach
    void setUp() {
        topicMapper = new TopicMapper();
        coachingTopicMapper = new CoachingTopicMapper(topicMapper);

        topicDto1 = TopicDto.Builder.aTopicDto()
                .withName("Biology")
                .build();
        topicDto2 = TopicDto.Builder.aTopicDto()
                .withName("Physics")
                .build();

        coachingTopicDto1 = CoachingTopicDto.Builder.aCoachingTopicDto()
                .withTopic(topicDto1)
                .withExperience(2.5)
                .build();

        coachingTopicDto2 = CoachingTopicDto.Builder.aCoachingTopicDto()
                .withTopic(topicDto2)
                .withExperience(4)
                .build();

        mappedTopic1 = topicMapper.toEntity(topicDto1);
        expectedCoachingTopic1 = new CoachingTopic(mappedTopic1, 2.5);
        mappedTopic2 = topicMapper.toEntity(topicDto2);
        expectedCoachingTopic2 = new CoachingTopic(mappedTopic2, 4);

    }

    @Test
    void givenCoachingTopicDto_WhenMapToEntity_ThenExpectCorrectEntity() {
        //given

        //when
        CoachingTopic actualCoachingTopic = coachingTopicMapper.toEntity(coachingTopicDto1);

        //then
        CoachingTopic expectCoachingTopic1 = new CoachingTopic(mappedTopic1, 2.5);

        assertEquals(expectCoachingTopic1.getTopic().getName(), actualCoachingTopic.getTopic().getName());
        assertEquals(expectCoachingTopic1.getExperience(), actualCoachingTopic.getExperience());
    }

    @Test
    void givenTwoCoachingTopicDto_WhenMapToEntity_ThenExpectTwoCorrectEntity() {
        //given

        //when
        List<CoachingTopic> actualListOfCoachingTopic = coachingTopicMapper.toEntity(List.of(coachingTopicDto1, coachingTopicDto2));

        //then
        assertEquals(2, actualListOfCoachingTopic.size());

        assertEquals(expectedCoachingTopic1.getTopic().getName(), actualListOfCoachingTopic.get(0).getTopic().getName());
        assertEquals(expectedCoachingTopic1.getExperience(), actualListOfCoachingTopic.get(0).getExperience());

        assertEquals(expectedCoachingTopic2.getTopic().getName(), actualListOfCoachingTopic.get(1).getTopic().getName());
        assertEquals(expectedCoachingTopic2.getExperience(), actualListOfCoachingTopic.get(1).getExperience());
    }
}