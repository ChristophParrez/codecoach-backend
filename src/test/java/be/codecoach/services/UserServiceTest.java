package be.codecoach.services;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.builder.UserTestBuilder;
import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.User;
import be.codecoach.exceptions.UserNotFoundException;
import be.codecoach.repositories.CoachingTopicRepository;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.UserRepository;
import be.codecoach.security.authentication.jwt.JwtGenerator;
import be.codecoach.security.authentication.user.api.AccountService;
import be.codecoach.services.mappers.CoachingTopicMapper;
import be.codecoach.services.mappers.RoleMapper;
import be.codecoach.services.mappers.UserMapper;
import be.codecoach.services.validators.MemberValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

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
    private AccountService accountServiceMock;
    private JwtGenerator jwtGeneratorMock;

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
        jwtGeneratorMock = Mockito.mock(JwtGenerator.class);
        userService = new UserService(userMapperMock, roleMapperMock, userRepositoryMock, memberValidatorMock,
                roleRepositoryMock, passwordEncoderMock, coachingTopicMapperMock, coachingTopicRepositoryMock,
                topicServiceMock, coachInformationServiceMock, authenticationServiceMock, jwtGeneratorMock);
    }

    @Test
    void givenUserDto_whenCallCreateAccount_ThenSaveMethodFromUserRepositoryIsCalled() {
        // GIVEN
        User user = UserTestBuilder.anUser().build();
        Role role = new Role(RoleEnum.COACHEE);
        UserDto userDto = UserDto.Builder.anUserDto().build();

        // WHEN
        doNothing().when(memberValidatorMock).validate(userDto);

        when(roleRepositoryMock.findByRole(RoleEnum.COACHEE)).thenReturn(role);
        when(userMapperMock.toEntity(userDto, role)).thenReturn(user);
        when(passwordEncoderMock.encode(any())).thenReturn(user.getPassword());

        userService.createAccount(userDto);

        // THEN
        Mockito.verify(userRepositoryMock).save(user);
    }

    @Test
    void givenUserEmail_whenCallFindByEmail_thenFindByEmailFromUserRepositoryIsCalled() {
        // GIVEN
        String email = "mert@gmail.com";

        // WHEN
        userService.findByEmail(email);

        // THEN
        Mockito.verify(userRepositoryMock).findByEmail(email);
    }

    @Test
    void givenUserEmail_whenCallExistsByEmail_thenExistsByEmailFromUserRepositoryIsCalled() {
        // GIVEN
        String email = "mert@gmail.com";

        // WHEN
        userService.existsByEmail(email);

        // THEN
        Mockito.verify(userRepositoryMock).existsByEmail(email);
    }

    @Test
    void givenPresentUserId_whenCallGetUser_thenFindByIdIsCalledFromUserRepository() {
        // GIVEN
        User user = UserTestBuilder.anUser().build();

        // WHEN
        when(userRepositoryMock.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        userService.getUser(user.getId());

        // THEN
        Mockito.verify(userRepositoryMock).findById(user.getId());
    }

    @Test
    void givenNonPresentUserId_whenCallGetUser_thenUserNotFoundExceptionIsThrown() {
        // GIVEN
        String userId = UUID.randomUUID().toString();

        // WHEN
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // THEN
        Assertions.assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.getUser(userId));
    }

    @Test
    void givenCoacheeUserId_whenCallGetCoacheeProfileDto_thenToCoacheeProfileDtoFromUserMapperIsCalled() {
        // GIVEN
        UserService userService = new UserService(userMapperMock, roleMapperMock, userRepositoryMock, memberValidatorMock,
                roleRepositoryMock, passwordEncoderMock, coachingTopicMapperMock, coachingTopicRepositoryMock,
                topicServiceMock, coachInformationServiceMock, authenticationServiceMock, jwtGeneratorMock);
        UserService userServiceSpy = Mockito.spy(userService);

        User user = UserTestBuilder.anUser().build();
        String userId = UUID.randomUUID().toString();

        // WHEN
        Mockito.doReturn(user).when(userServiceSpy).getUser(userId);
        when(userMapperMock.toCoacheeProfileDto(user)).thenReturn(any(UserDto.class));
        userServiceSpy.getCoacheeProfileDto(userId);

        // THEN
        Mockito.verify(userMapperMock).toCoacheeProfileDto(user);
    }

    @Test
    void givenCoachUserId_whenCallGetCoachProfileDto_thenToCoachProfileDtoFromUserMapperIsCalled() {
        // GIVEN
        UserService userService = new UserService(userMapperMock, roleMapperMock, userRepositoryMock, memberValidatorMock,
                roleRepositoryMock, passwordEncoderMock, coachingTopicMapperMock, coachingTopicRepositoryMock,
                topicServiceMock, coachInformationServiceMock, authenticationServiceMock, jwtGeneratorMock);
        UserService userServiceSpy = Mockito.spy(userService);

        User user = UserTestBuilder.anUser().build();
        String userId = UUID.randomUUID().toString();

        // WHEN
        Mockito.doReturn(user).when(userServiceSpy).getUser(userId);
        when(userMapperMock.toCoacheeProfileDto(user)).thenReturn(any(UserDto.class));
        userServiceSpy.getCoachProfileDto(userId);

        // THEN
        Mockito.verify(userMapperMock).toCoachProfileDto(user);
    }
    @Test
    void givenUserIdThatBelongsToAUserWhoHasNullCoachInformation_whenCallGetCoachProfileDto_thenToCoacheeProfileDtoFromUserMapperIsCalled() {
        // GIVEN
        UserService userService = new UserService(userMapperMock, roleMapperMock, userRepositoryMock, memberValidatorMock,
                roleRepositoryMock, passwordEncoderMock, coachingTopicMapperMock, coachingTopicRepositoryMock,
                topicServiceMock, coachInformationServiceMock, authenticationServiceMock, jwtGeneratorMock);
        UserService userServiceSpy = Mockito.spy(userService);

        User user = UserTestBuilder.anEmptyUser().build();
        String userId = UUID.randomUUID().toString();

        // WHEN
        Mockito.doReturn(user).when(userServiceSpy).getUser(userId);
        when(userMapperMock.toCoacheeProfileDto(user)).thenReturn(any(UserDto.class));
        userServiceSpy.getCoachProfileDto(userId);

        // THEN
        Mockito.verify(userMapperMock).toCoacheeProfileDto(user);
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