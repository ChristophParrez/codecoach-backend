package be.codecoach.services.mappers;

import be.codecoach.api.dtos.StatusDto;
import be.codecoach.domain.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {

    public Status toEntity(StatusDto dto) {
        return new Status(dto.getStatusName());
    }

    public StatusDto toDto(Status status) {
        return StatusDto.Builder.aStatusDto().withStatusName(status.getStatusName()).build();
    }
}
