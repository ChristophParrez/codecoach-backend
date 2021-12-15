package be.codecoach.services.mappers;

import be.codecoach.api.dtos.LocationDto;
import be.codecoach.domain.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public Location toEntity(LocationDto dto) {
        return new Location(dto.getName());
    }

    public LocationDto toDto(Location location) {
        return LocationDto.Builder.aLocationDto().withName(location.getName()).build();
    }
}
