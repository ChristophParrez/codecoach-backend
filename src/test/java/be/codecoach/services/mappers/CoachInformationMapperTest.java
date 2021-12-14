package be.codecoach.services.mappers;

import be.codecoach.api.dtos.CoachInformationDto;
import be.codecoach.api.dtos.CoachingTopicDto;
import be.codecoach.builder.CoachInformationTestBuilder;
import be.codecoach.builder.CoachingTopicTestBuilder;
import be.codecoach.domain.CoachInformation;
import be.codecoach.domain.CoachingTopic;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoachInformationMapperTest {

    @Mock
    private CoachingTopicMapper coachingTopicMapperMock;

    @Mock
    private CoachingTopicMapper getCoachingTopicMapperMock;

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenCoachingInformationDto_whenMapToCoachingInformation_ThenExpectCorrectCoachingInformation() {
        // GIVEN
        List<CoachingTopic> coachingTopicList = List.of(CoachingTopicTestBuilder.aCoachingTopic().build());
        List<CoachingTopicDto> coachingTopicDtoList = List.of(CoachingTopicDto.Builder.aCoachingTopicDto().build());

        when(coachingTopicMapperMock.toEntity(coachingTopicDtoList)).thenReturn(coachingTopicList);

        // WHEN
        CoachInformation coachInformation = new CoachInformationMapper(coachingTopicMapperMock).toEntity(
                CoachInformationDto.Builder.aCoachInformationDto()
                        .withCoachInfoId("12345")
                        .withCoachXp(20)
                        .withIntroduction("TestIntro")
                        .withAvailability("available")
                        .withCoachingTopics(coachingTopicDtoList)
                        .build()
        );

        CoachInformation expected = CoachInformationTestBuilder.aCoachInformation().build();

        // THEN
        Assertions.assertThat(expected.getCoachXp()).isEqualTo(coachInformation.getCoachXp());
        Assertions.assertThat(expected.getAvailability()).isEqualTo(coachInformation.getAvailability());
        Assertions.assertThat(expected.getIntroduction()).isEqualTo(coachInformation.getIntroduction());
        Assertions.assertThat(expected.getId()).isEqualTo(coachInformation.getId());
    }

    @Test
    void givenCoachingInformation_whenMapToCoachingInformationDto_ThenExpectCorrectCoachingInformationDto() {
        // GIVEN
        List<CoachingTopic> coachingTopicList = List.of(CoachingTopicTestBuilder.aCoachingTopic().build());
        List<CoachingTopicDto> coachingTopicDtoList = List.of(CoachingTopicDto.Builder.aCoachingTopicDto().build());

        when(coachingTopicMapperMock.toDto(coachingTopicList)).thenReturn(coachingTopicDtoList);

        // WHEN
        CoachInformationDto actual = new CoachInformationMapper(coachingTopicMapperMock).toDto(
                CoachInformation.CoachInformationBuilder.coachInformation()
                        .withCoachInfoId("12345")
                        .withCoachXp(20)
                        .withCoachingTopics(coachingTopicList)
                        .withAvailability("available")
                        .withIntroduction("TestIntro").build()

        );

        CoachInformationDto expected = CoachInformationDto.Builder.aCoachInformationDto()
                .withCoachInfoId("12345")
                .withCoachXp(20)
                .withIntroduction("TestIntro")
                .withAvailability("available")
                .withCoachingTopics(coachingTopicDtoList)
                .build();

        // THEN
        Assertions.assertThat(actual.getCoachInfoId()).isEqualTo(null);
        Assertions.assertThat(actual.getCoachXp()).isEqualTo(expected.getCoachXp());
        Assertions.assertThat(actual.getAvailability()).isEqualTo(expected.getAvailability());
        Assertions.assertThat(actual.getIntroduction()).isEqualTo(expected.getIntroduction());
    }
}