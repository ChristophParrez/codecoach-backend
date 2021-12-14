package be.codecoach.repositories;

import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.save(new Role(RoleEnum.COACH));
        roleRepository.save(new Role(RoleEnum.COACHEE));
        roleRepository.save(new Role(RoleEnum.ADMIN));
    }

    @Test
    void givenPopulatedRoleTable_whenFindByRoleWithCoacheeArgument_thenExpectCorrectRole() {
        //when
        Role roleFromRepo = roleRepository.findByRole(RoleEnum.COACHEE);

        //then
        Role expectedRole = new Role(RoleEnum.COACHEE);
        assertEquals(expectedRole.getRole(), roleFromRepo.getRole());
    }

    @Test
    void givenPopulatedRoleTable_whenFindByRoleWithCoachArgument_thenExpectCorrectRole() {
        //when
        Role roleFromRepo = roleRepository.findByRole(RoleEnum.COACH);

        //then
        Role expectedRole = new Role(RoleEnum.COACH);
        assertEquals(expectedRole.getRole(), roleFromRepo.getRole());
    }

    @Test
    void givenPopulatedRoleTable_whenFindByRoleWithAdminArgument_thenExpectCorrectRole() {
        //when
        Role roleFromRepo = roleRepository.findByRole(RoleEnum.ADMIN);

        //then
        Role expectedRole = new Role(RoleEnum.ADMIN);
        assertEquals(expectedRole.getRole(), roleFromRepo.getRole());
    }
}