package be.codecoach.api.dtos;

public class FeedbackDto {

    private final String id;
    private final String feedbackGiverId;
    private final double score1;
    private final double score2;
    private final String comment;

    public FeedbackDto(String id, String feedbackGiverId, double score1, double score2, String comment) {
        this.id = id;
        this.feedbackGiverId = feedbackGiverId;
        this.score1 = score1;
        this.score2 = score2;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getFeedbackGiverId() {
        return feedbackGiverId;
    }

    public double getScore1() {
        return score1;
    }

    public double getScore2() {
        return score2;
    }

    public String getComment() {
        return comment;
    }


    public static final class Builder {
        private String id;
        private String feedbackGiverId;
        private double score1;
        private double score2;
        private String comment;

        private Builder() {
        }

        public static Builder aFeedbackDto() {
            return new Builder();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withFeedbackGiverId(String feedbackGiverId) {
            this.feedbackGiverId = feedbackGiverId;
            return this;
        }

        public Builder withScore1(double score1) {
            this.score1 = score1;
            return this;
        }

        public Builder withScore2(double score2) {
            this.score2 = score2;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public FeedbackDto build() {
            return new FeedbackDto(id, feedbackGiverId, score1, score2, comment);
        }
    }
}
