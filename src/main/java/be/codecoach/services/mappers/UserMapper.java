package be.codecoach.services.mappers;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.CoachInformation;
import be.codecoach.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import be.codecoach.domain.User;

import java.util.List;

@Component
public class UserMapper {

    private final CoachInformationMapper coachInformationMapper;
    private final RoleMapper roleMapper;

    @Autowired
    public UserMapper(CoachInformationMapper coachInformationMapper, RoleMapper roleMapper) {
        this.coachInformationMapper = coachInformationMapper;
        this.roleMapper = roleMapper;
    }

    public User toEntity(UserDto dto) {
        CoachInformation coachInformation = coachInformationMapper.toEntity(dto.getCoachInformation());
        List<Role> roles = roleMapper.toEntity(dto.getRoles());

        return new User(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getCompanyName(), dto.getPassword(), roles, dto.getPicture(), coachInformation);
    }
}
