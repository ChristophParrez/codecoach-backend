package be.codecoach.api.dtos;

import java.util.List;

public class CoachInformationDto {

    private final String coachInfoId;
    private final int coachXp;
    private final String introduction;
    private final String availability;
    private final List<CoachingTopicDto> coachingTopics;

    private CoachInformationDto(String coachInfoId, int coachXp, String introduction, String availability, List<CoachingTopicDto> coachingTopics) {
        this.coachInfoId = coachInfoId;
        this.coachXp = coachXp;
        this.introduction = introduction;
        this.availability = availability;
        this.coachingTopics = coachingTopics;
    }

    public String getCoachInfoId() {
        return coachInfoId;
    }

    public int getCoachXp() {
        return coachXp;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getAvailability() {
        return availability;
    }

    public List<CoachingTopicDto> getCoachingTopics() {
        return coachingTopics;
    }

    public static final class Builder {
        private String coachInfoId;
        private int coachXp;
        private String introduction;
        private String availability;
        private List<CoachingTopicDto> coachingTopics;

        private Builder() {
        }

        public static Builder aCoachInformationDto() {
            return new Builder();
        }

        public Builder withCoachInfoId(String coachInfoId) {
            this.coachInfoId = coachInfoId;
            return this;
        }

        public Builder withCoachXp(int coachXp) {
            this.coachXp = coachXp;
            return this;
        }

        public Builder withIntroduction(String introduction) {
            this.introduction = introduction;
            return this;
        }

        public Builder withAvailability(String availability) {
            this.availability = availability;
            return this;
        }

        public Builder withCoachingTopics(List<CoachingTopicDto> coachingTopics) {
            this.coachingTopics = coachingTopics;
            return this;
        }

        public CoachInformationDto build() {
            return new CoachInformationDto(coachInfoId, coachXp, introduction, availability, coachingTopics);
        }
    }
}
