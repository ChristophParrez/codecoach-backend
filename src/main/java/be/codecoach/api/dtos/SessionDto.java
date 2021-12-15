package be.codecoach.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class SessionDto {

    private final String id;
    private final String coachId;
    private final String coacheeId;
    private final String subject;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private final LocalTime time;
    private final LocationDto location;
    private final String remarks;
    private final StatusDto status;
    private final FeedbackDto coachFeedback;
    private final FeedbackDto coacheeFeedback;

    public SessionDto(String id, String coachId, String coacheeId, String subject, LocalDate date, LocalTime time, LocationDto location, String remarks, StatusDto status, FeedbackDto coachFeedback, FeedbackDto coacheeFeedback) {
        this.id = id;
        this.coachId = coachId;
        this.coacheeId = coacheeId;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.location = location;
        this.remarks = remarks;
        this.status = status;
        this.coachFeedback = coachFeedback;
        this.coacheeFeedback = coacheeFeedback;
    }

    public String getId() {
        return id;
    }

    public String getCoachId() {
        return coachId;
    }

    public String getCoacheeId() {
        return coacheeId;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocationDto getLocation() {
        return location;
    }

    public String getRemarks() {
        return remarks;
    }

    public StatusDto getStatus() {
        return status;
    }

    public FeedbackDto getCoachFeedback() {
        return coachFeedback;
    }

    public FeedbackDto getCoacheeFeedback() {
        return coacheeFeedback;
    }


    public static final class Builder {
        private String id;
        private String coachId;
        private String coacheeId;
        private String subject;
        private LocalDate date;
        private LocalTime time;
        private LocationDto location;
        private String remarks;
        private StatusDto status;
        private FeedbackDto coachFeedback;
        private FeedbackDto coacheeFeedback;

        private Builder() {
        }

        public static Builder aSessionDto() {
            return new Builder();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withCoachId(String coachId) {
            this.coachId = coachId;
            return this;
        }

        public Builder withCoacheeId(String coacheeId) {
            this.coacheeId = coacheeId;
            return this;
        }

        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder withTime(LocalTime time) {
            this.time = time;
            return this;
        }

        public Builder withLocation(LocationDto location) {
            this.location = location;
            return this;
        }

        public Builder withRemarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder withStatus(StatusDto status) {
            this.status = status;
            return this;
        }

        public Builder withCoachFeedback(FeedbackDto coachFeedback) {
            this.coachFeedback = coachFeedback;
            return this;
        }

        public Builder withCoacheeFeedback(FeedbackDto coacheeFeedback) {
            this.coacheeFeedback = coacheeFeedback;
            return this;
        }

        public SessionDto build() {
            return new SessionDto(id, coachId, coacheeId, subject, date, time, location, remarks, status, coachFeedback, coacheeFeedback);
        }
    }
}
