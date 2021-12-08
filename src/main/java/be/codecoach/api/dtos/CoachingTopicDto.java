package be.codecoach.api.dtos;

import be.codecoach.domain.Topic;

public class CoachingTopicDto {

    private final String coachingTopicId;
    private final TopicDto topic;
    private final double experience;

    private CoachingTopicDto(String coachingTopicId, TopicDto topic, double experience) {
        this.coachingTopicId = coachingTopicId;
        this.topic = topic;
        this.experience = experience;
    }

    public String getCoachingTopicId() {
        return coachingTopicId;
    }

    public TopicDto getTopic() {
        return topic;
    }

    public double getExperience() {
        return experience;
    }

    public static final class Builder {
        private String coachingTopicId;
        private TopicDto topic;
        private double experience;

        private Builder() {
        }

        public static Builder aCoachingTopicDto() {
            return new Builder();
        }

        public Builder withCoachingTopicId(String coachingTopicId) {
            this.coachingTopicId = coachingTopicId;
            return this;
        }

        public Builder withTopic(TopicDto topic) {
            this.topic = topic;
            return this;
        }

        public Builder withExperience(double experience) {
            this.experience = experience;
            return this;
        }

        public CoachingTopicDto build() {
            return new CoachingTopicDto(coachingTopicId, topic, experience);
        }
    }
}
