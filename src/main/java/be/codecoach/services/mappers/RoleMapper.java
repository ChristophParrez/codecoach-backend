package be.codecoach.services.mappers;

import be.codecoach.api.dtos.RoleDto;
import be.codecoach.domain.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public List<Role> toEntity(List<RoleDto> dto) {
        return dto.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public Role toEntity(RoleDto dto) {
        return new Role(dto.getRole());
    }

    public RoleDto toDto(Role role) { return RoleDto.Builder.aRoleDto()
            .withRoleId(role.getRoleId())
            .withRole(role.getRole())
            .build();}

    public List<RoleDto> toDto(List<Role> roles) {
        return roles.stream().map(this::toDto).collect(Collectors.toList());
    }
}
