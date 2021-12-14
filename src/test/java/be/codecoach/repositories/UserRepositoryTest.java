package be.codecoach.repositories;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    String email;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        email = "John@Doe.com";
        userRepository.save(new User("","",email,"","",null,"",null));
    }

    @Test
    void findByEmail() {
        assertTrue(userRepository.findByEmail(email).isPresent());
    }

    @Test
    void existsByEmail() {
        assertTrue(userRepository.existsByEmail(email));
    }
}