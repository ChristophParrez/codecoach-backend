package be.codecoach.api;

import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.User;
import be.codecoach.repositories.UserRepository;
import be.codecoach.security.authentication.jwt.JwtGenerator;
import be.codecoach.security.authentication.user.SecuredUserService;
import be.codecoach.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    WebApplicationContext wac;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserService userService;

    @MockBean
    SecuredUserService securedUserService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    JwtGenerator jwtGenerator;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    void getUser() throws Exception {
        mockMvc.perform(get("/users/123456")).andExpect(status().isOk());
    }

    @Test
    void registerUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .content(new ObjectMapper().writeValueAsString(new User("Glenn", "Verhaeghe", "gle@ver.be", "egov", "Pass1234", Set.of(new Role(RoleEnum.COACHEE)), null, null)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }


}