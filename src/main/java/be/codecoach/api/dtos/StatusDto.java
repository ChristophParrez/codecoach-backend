package be.codecoach.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusDto {

    private final String statusName;

    private StatusDto(@JsonProperty("statusName") String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public static final class Builder {
        private String statusName;

        private Builder() {
        }

        public static Builder aStatusDto() {
            return new Builder();
        }

        public Builder withStatusName(String statusName) {
            this.statusName = statusName;
            return this;
        }

        public StatusDto build() {
            return new StatusDto(statusName);
        }
    }
}
