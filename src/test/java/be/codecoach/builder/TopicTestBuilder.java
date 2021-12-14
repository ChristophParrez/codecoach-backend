package be.codecoach.builder;

import be.codecoach.domain.Builder;
import be.codecoach.domain.Topic;
import be.codecoach.domain.Topic.TopicBuilder;

public class TopicTestBuilder extends Builder<Topic> {

    private TopicBuilder topicBuilder;

    public TopicTestBuilder(TopicBuilder topicBuilder) {
        this.topicBuilder = topicBuilder;
    }

    public static TopicTestBuilder anEmptyTopic() {
        return new TopicTestBuilder(TopicBuilder.topic());
    }

    public static TopicTestBuilder aTopic() {
        return new TopicTestBuilder(TopicBuilder.topic()
                //.withId("12345")
                .withName("math"));
    }

    @Override
    public Topic build() {
        return topicBuilder.build();
    }
}
