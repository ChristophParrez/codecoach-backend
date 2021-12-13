package be.codecoach.builder;

import be.codecoach.domain.Builder;
import be.codecoach.domain.CoachingTopic;
import be.codecoach.domain.CoachingTopic.CoachingTopicBuilder;

import static be.codecoach.builder.TopicTestBuilder.aTopic;

public class CoachingTopicTestBuilder extends Builder<CoachingTopic> {

    private CoachingTopicBuilder coachingTopicBuilder;

    public CoachingTopicTestBuilder(CoachingTopicBuilder coachingTopicBuilder) {
        this.coachingTopicBuilder = coachingTopicBuilder;
    }

    public static CoachingTopicTestBuilder anEmptyCoachingTopic() {
        return new CoachingTopicTestBuilder(CoachingTopicBuilder.coachingTopic());
    }

    public static CoachingTopicTestBuilder aCoachingTopic() {
        return new CoachingTopicTestBuilder(CoachingTopicBuilder.coachingTopic()
                .withCoachingTopicId("12345")
                .withTopic(aTopic().build())
                .withExperience(3));
    }

    @Override
    public CoachingTopic build() {
        return coachingTopicBuilder.build();
    }
}
