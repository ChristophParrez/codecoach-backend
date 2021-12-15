package be.codecoach.services;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.builder.UserTestBuilder;
import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.User;
import be.codecoach.repositories.CoachingTopicRepository;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.UserRepository;
import be.codecoach.services.mappers.CoachingTopicMapper;
import be.codecoach.services.mappers.RoleMapper;
import be.codecoach.services.mappers.UserMapper;
import be.codecoach.services.validators.MemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserService userService;
    private UserMapper userMapperMock;
    private RoleMapper roleMapperMock;
    private UserRepository userRepositoryMock;
    private MemberValidator memberValidatorMock;
    private RoleRepository roleRepositoryMock;
    private PasswordEncoder passwordEncoderMock;
    private CoachingTopicMapper coachingTopicMapperMock;
    private CoachingTopicRepository coachingTopicRepositoryMock;
    private TopicService topicServiceMock;
    private CoachInformationService coachInformationServiceMock;
    private AuthenticationService authenticationServiceMock;


    @BeforeEach
    void setUp() {
        userMapperMock = Mockito.mock(UserMapper.class);
        userRepositoryMock = Mockito.mock(UserRepository.class);
        memberValidatorMock = Mockito.mock(MemberValidator.class);
        roleRepositoryMock = Mockito.mock(RoleRepository.class);
        passwordEncoderMock = Mockito.mock(PasswordEncoder.class);
        coachingTopicMapperMock = Mockito.mock(CoachingTopicMapper.class);
        coachingTopicRepositoryMock = Mockito.mock(CoachingTopicRepository.class);
        topicServiceMock = Mockito.mock(TopicService.class);
        coachInformationServiceMock = Mockito.mock(CoachInformationService.class);
        roleMapperMock = Mockito.mock(RoleMapper.class);
        authenticationServiceMock = Mockito.mock(AuthenticationService.class);
        userService = new UserService(userMapperMock, roleMapperMock, userRepositoryMock, memberValidatorMock,
                roleRepositoryMock, passwordEncoderMock, coachingTopicMapperMock, coachingTopicRepositoryMock,
                topicServiceMock, coachInformationServiceMock, authenticationServiceMock);
    }

    @Test
    void registerUser() {
        // GIVEN
        User user = UserTestBuilder.anUser().build();
        Role role = new Role(RoleEnum.COACHEE);
        UserDto userDto = UserDto.Builder.anUserDto().build();

        // WHEN
        doNothing().when(memberValidatorMock).validate(userDto);

        when(roleRepositoryMock.findByRole(RoleEnum.COACHEE)).thenReturn(role);
        when(userMapperMock.toEntity(userDto, role)).thenReturn(user);
        when(passwordEncoderMock.encode(any())).thenReturn(user.getPassword());

        userService.registerUser(userDto);

        // THEN
        Mockito.verify(userRepositoryMock).save(user);
    }

    @Test
    void findByEmail() {
    }

    @Test
    void createAccount() {
    }

    @Test
    void existsByEmail() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getCoacheeProfileDto() {
    }

    @Test
    void getCoachProfileDto() {
    }

    @Test
    void becomeCoach() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void updateCoach() {
    }

    @Test
    void addTopic() {
    }
}