package be.codecoach.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopicDto {

    private final String name;

    private TopicDto(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static final class Builder {
        private String name;

        private Builder() {
        }

        public static Builder aTopicDto() {
            return new Builder();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public TopicDto build() {
            return new TopicDto(name);
        }
    }
}
