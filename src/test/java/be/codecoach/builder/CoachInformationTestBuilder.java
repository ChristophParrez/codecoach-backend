package be.codecoach.builder;

import be.codecoach.domain.Builder;
import be.codecoach.domain.CoachInformation;
import be.codecoach.domain.CoachInformation.CoachInformationBuilder;

import java.util.List;

import static be.codecoach.builder.CoachingTopicTestBuilder.aCoachingTopic;

public class CoachInformationTestBuilder extends Builder<CoachInformation> {

    private CoachInformationBuilder coachInformationBuilder;

    public CoachInformationTestBuilder(CoachInformationBuilder coachInformationBuilder) {
        this.coachInformationBuilder = coachInformationBuilder;
    }

    public static CoachInformationTestBuilder anEmptyCoachInformation() {
        return new CoachInformationTestBuilder(CoachInformationBuilder.coachInformation());
    }

    public static CoachInformationTestBuilder aCoachInformation() {
        return new CoachInformationTestBuilder(CoachInformationBuilder.coachInformation()
                .withCoachInfoId("12345")
                .withCoachXp(20)
                .withIntroduction("TestIntro")
                .withAvailability("available")
                .withCoachingTopics(List.of(aCoachingTopic().build())));
    }

    @Override
    public CoachInformation build() {
        return coachInformationBuilder.build();
    }
}
