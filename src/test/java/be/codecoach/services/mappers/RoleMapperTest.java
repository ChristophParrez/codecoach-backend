package be.codecoach.services.mappers;

import be.codecoach.api.dtos.RoleDto;
import be.codecoach.builder.RoleTestBuilder;
import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleMapperTest {

    Role coachee;
    Role coach;
    Role admin;
    RoleDto coacheeDto;
    RoleDto coachDto;
    RoleDto adminDto;
    List<RoleEnum> roleEnums;

    @BeforeEach
    void setup() {

        coacheeDto = RoleDto.Builder.aRoleDto().withRole(RoleEnum.COACHEE).build();
        coachee = new RoleMapper().toEntity(coacheeDto);

        coachDto = RoleDto.Builder.aRoleDto().withRole(RoleEnum.COACH).build();
        coach = new RoleMapper().toEntity(coachDto);

        adminDto = RoleDto.Builder.aRoleDto().withRole(RoleEnum.ADMIN).build();
        admin = new RoleMapper().toEntity(adminDto);

        roleEnums = List.of(RoleEnum.COACHEE, RoleEnum.COACH, RoleEnum.ADMIN);
    }

    @Test
    void givenRoleDtoWithRoleCoachee_WhenMapToEntity_ThenExpectCorrectEntity() {
        Assertions.assertThat(coachee.getRole().toString()).isEqualTo("COACHEE");
    }

    @Test
    void givenRoleDtoWithRoleCoach_WhenMapToEntity_ThenExpectCorrectEntity() {
        Assertions.assertThat(coach.getRole().toString()).isEqualTo("COACH");
    }

    @Test
    void givenRoleDtoWithRoleAdmin_WhenMapToEntity_ThenExpectCorrectEntity() {
        Assertions.assertThat(admin.getRole().toString()).isEqualTo("ADMIN");
    }

    @Test
    void givenSetOfThreeRoleDto_WhenMapToEntity_ThenExpectCorrectSetOfEntities() {

        // GIVEN
        Set<RoleDto> roleDtoSet = Set.of(coacheeDto, coachDto, adminDto);

        // WHEN
        Set<Role> result = new RoleMapper().toEntity(roleDtoSet);

        // THEN
        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result).extracting(record -> record.getRole()).containsExactlyInAnyOrderElementsOf(roleEnums);
    }

    @Test
    void givenARoleCoachee_WhenMapToRoleDto_ThenExpectCorrectRoleDto() {
        // GIVEN

        // WHEN
        RoleDto result = new RoleMapper().toDto(RoleTestBuilder.aRole().build());

        // THEN
        Assertions.assertThat("COACHEE").isEqualTo(result.getRole().toString());
        Assertions.assertThat(0).isEqualTo(result.getRoleId());
    }

    @Test
    void givenASetOfRoles_WhenMapToDto_ThenExpectCorrectSetOfRoleDtos() {
        // GIVEN
        Set<Role> roleSet = Set.of(RoleTestBuilder.aRoleWithCoachee().build(), RoleTestBuilder.aRoleWithCoach().build(), RoleTestBuilder.aRoleWithAdmin().build());

        // WHEN
        Set<RoleDto> result = new RoleMapper().toDto(roleSet);

        // THEN
        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result).extracting(record -> record.getRole()).containsExactlyInAnyOrderElementsOf(roleEnums);
        Assertions.assertThat(result).extracting(record -> record.getRoleId()).containsExactlyInAnyOrderElementsOf(List.of(0, 0, 0));
    }
}