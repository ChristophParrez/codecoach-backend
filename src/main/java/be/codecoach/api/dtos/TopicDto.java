package be.codecoach.api.dtos;

import javax.persistence.Column;

public class TopicDto {

    private final String topicId;
    private final String name;

    private TopicDto(String topicId, String name) {
        this.topicId = topicId;
        this.name = name;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getName() {
        return name;
    }

    public static final class Builder {
        private String topic_id;
        private String name;

        private Builder() {
        }

        public static Builder aTopicDto() {
            return new Builder();
        }

        public Builder withTopic_id(String topic_id) {
            this.topic_id = topic_id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public TopicDto build() {
            return new TopicDto(topic_id, name);
        }
    }
}
