package be.codecoach.api;

import be.codecoach.repositories.UserRepository;
import be.codecoach.security.authentication.jwt.JwtGenerator;
import be.codecoach.security.authentication.user.SecuredUserService;
import be.codecoach.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
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


}