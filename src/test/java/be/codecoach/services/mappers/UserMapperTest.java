package be.codecoach.services.mappers;

import be.codecoach.api.dtos.CoachInformationDto;
import be.codecoach.api.dtos.RoleDto;
import be.codecoach.api.dtos.UserDto;
import be.codecoach.builder.CoachInformationTestBuilder;
import be.codecoach.builder.RoleTestBuilder;
import be.codecoach.builder.UserTestBuilder;
import be.codecoach.domain.CoachInformation;
import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static be.codecoach.builder.CoachInformationTestBuilder.aCoachInformation;
import static be.codecoach.builder.RoleTestBuilder.aRole;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @BeforeEach
    void setUp() {
    }

    @Mock
    private CoachInformationMapper coachInformationMapperMock;

    @Mock
    private RoleMapper roleMapperMock;

    @Test
    void givenRoleAndUserDto_whenMapToEntity_thenExpectCorrectUser() {
        // GIVEN
        CoachInformationDto coachInformationDto = CoachInformationDto.Builder.aCoachInformationDto().build();
        CoachInformation coachInformation = CoachInformationTestBuilder.aCoachInformation().build();
        Role role = RoleTestBuilder.aRoleWithCoachee().build();

        UserDto userDto = UserDto.Builder.anUserDto()
                .withFirstName("Mert")
                .withLastName("Demirok")
                .withEmail("mertdemirok@gmail.com")
                .withCompanyName("FOD ECO")
                .withPassword("Password123")
                .withRoles(Set.of(RoleDto.Builder.aRoleDto().withRole(RoleEnum.COACHEE).build()))
                .withCoachInformation(coachInformationDto)
                .build();

        // WHEN
        when(coachInformationMapperMock.toEntity(coachInformationDto)).thenReturn(coachInformation);
        User actual = new UserMapper(coachInformationMapperMock, roleMapperMock).toEntity(userDto, role);
        User expected = UserTestBuilder.anUser().build();

        // THEN
        Assertions.assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getCompanyName()).isEqualTo(expected.getCompanyName());
        Assertions.assertThat(actual.getRoles()).isEqualTo(expected.getRoles());
        Assertions.assertThat(actual.getCoachInformation()).isNotNull();
        Assertions.assertThat(actual.getId()).isNull();
    }

    @Test
    void givenUser_whenMapToCoacheeProfileDto_thenExpectCorrectUserDto() {
        // GIVEN
        User user = UserTestBuilder.anUser().build();
        Set<RoleDto> roleDtoSet = Set.of(RoleDto.Builder.aRoleDto().withRole(RoleEnum.COACHEE).build());

        // WHEN
        when(roleMapperMock.toDto(anySet())).thenReturn(roleDtoSet);

        UserDto actual = new UserMapper(coachInformationMapperMock, roleMapperMock).toCoacheeProfileDto(user);
        UserDto expected = UserDto.Builder.anUserDto()
                .withUserId("12345")
                .withFirstName("Mert")
                .withLastName("Demirok")
                .withEmail("mertdemirok@gmail.com")
                .withCompanyName("FOD ECO")
                .withRoles(roleDtoSet)
                .withPicture("url")
                .build();

        // THEN
        Assertions.assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        Assertions.assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getCompanyName()).isEqualTo(expected.getCompanyName());
        Assertions.assertThat(actual.getRoles()).isEqualTo(expected.getRoles());
        Assertions.assertThat(actual.getCoachInformation()).isNull();
    }

    @Test
    void givenUser_whenMapToCoachProfileDto_thenExpectCorrectUserDto() {
        // GIVEN
        User user = UserTestBuilder.anUser().build();
        Set<RoleDto> roleDtoSet = Set.of(RoleDto.Builder.aRoleDto().withRole(RoleEnum.COACHEE).build());
        CoachInformationDto coachInformationDto = CoachInformationDto.Builder.aCoachInformationDto().build();

        // WHEN
        when(roleMapperMock.toDto(anySet())).thenReturn(roleDtoSet);
        when(coachInformationMapperMock.toDto(any(CoachInformation.class))).thenReturn(coachInformationDto);


        UserDto actual = new UserMapper(coachInformationMapperMock, roleMapperMock).toCoachProfileDto(user);
        UserDto expected = UserDto.Builder.anUserDto()
                .withUserId("12345")
                .withFirstName("Mert")
                .withLastName("Demirok")
                .withEmail("mertdemirok@gmail.com")
                .withCompanyName("FOD ECO")
                .withRoles(roleDtoSet)
                .withPicture("url")
                .withCoachInformation(coachInformationDto)
                .build();

        // THEN
        Assertions.assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        Assertions.assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getCompanyName()).isEqualTo(expected.getCompanyName());
        Assertions.assertThat(actual.getRoles()).isEqualTo(expected.getRoles());
        Assertions.assertThat(actual.getCoachInformation()).isNotNull();
    }
}