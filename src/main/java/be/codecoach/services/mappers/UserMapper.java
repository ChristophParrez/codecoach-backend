package be.codecoach.services.mappers;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.CoachInformation;
import be.codecoach.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import be.codecoach.domain.User;

import javax.persistence.SecondaryTable;
import java.util.List;
import java.util.Set;

@Component
public class UserMapper {

    private final CoachInformationMapper coachInformationMapper;
    private final RoleMapper roleMapper;

    @Autowired
    public UserMapper(CoachInformationMapper coachInformationMapper, RoleMapper roleMapper) {
        this.coachInformationMapper = coachInformationMapper;
        this.roleMapper = roleMapper;
    }

    public User toEntity(UserDto dto, Role role) {
        CoachInformation coachInformation = dto.getCoachInformation() != null ? coachInformationMapper.toEntity(dto.getCoachInformation()) : null;
        Set<Role> roles = Set.of(role);

        return new User(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getCompanyName(), dto.getPassword(), roles, dto.getPicture(), coachInformation);
    }

    public UserDto toCoacheeProfileDto(User user) {
        return UserDto.Builder.anUserDto()
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withEmail(user.getEmail())
                .withCompanyName(user.getCompanyName())
                .withRoles(roleMapper.toDto(user.getRoles()))
                .withPicture(user.getPicture())
                .build();
    }

    public UserDto toCoachProfileDto(User user) {
        return UserDto.Builder.anUserDto()
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withEmail(user.getEmail())
                .withCompanyName(user.getCompanyName())
                .withRoles(roleMapper.toDto(user.getRoles()))
                .withPicture(user.getPicture())
                .withCoachInformation(coachInformationMapper.toDto(user.getCoachInformation()))
                .build();
    }
}
