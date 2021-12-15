package be.codecoach.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationDto {

    private final String name;

    private LocationDto(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static final class Builder {
        private String name;

        private Builder() {
        }

        public static Builder aLocationDto() {
            return new Builder();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public LocationDto build() {
            return new LocationDto(name);
        }
    }
}
