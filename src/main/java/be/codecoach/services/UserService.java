package be.codecoach.services;

import be.codecoach.api.dtos.CoachInformationDto;
import be.codecoach.api.dtos.CoachingTopicDto;
import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.*;
import be.codecoach.exceptions.CoachingTopicException;
import be.codecoach.exceptions.TopicException;
import be.codecoach.exceptions.UserNotFoundException;
import be.codecoach.exceptions.WrongRoleException;
import be.codecoach.repositories.CoachingTopicRepository;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.UserRepository;
import be.codecoach.security.authentication.jwt.JwtGenerator;
import be.codecoach.security.authentication.user.api.Account;
import be.codecoach.security.authentication.user.api.AccountService;
import be.codecoach.services.mappers.CoachingTopicMapper;
import be.codecoach.services.mappers.RoleMapper;
import be.codecoach.services.mappers.UserMapper;
import be.codecoach.services.validators.MemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements AccountService {

    private static final String FORBIDDEN_ACCESS_MESSAGE = "You cannot change someone else's profile!";

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;
    private final MemberValidator memberValidator;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CoachingTopicMapper coachingTopicMapper;
    private final CoachingTopicRepository coachingTopicRepository;
    private final TopicService topicService;
    private final CoachInformationService coachInformationService;
    private final AuthenticationService authenticationService;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public UserService(UserMapper userMapper, RoleMapper roleMapper, UserRepository userRepository, MemberValidator memberValidator, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CoachingTopicMapper coachingTopicMapper, CoachingTopicRepository coachingTopicRepository, TopicService topicService, CoachInformationService coachInformationService, AuthenticationService authenticationService, JwtGenerator jwtGenerator) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRepository = userRepository;
        this.memberValidator = memberValidator;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.coachingTopicMapper = coachingTopicMapper;
        this.coachingTopicRepository = coachingTopicRepository;
        this.topicService = topicService;
        this.coachInformationService = coachInformationService;
        this.authenticationService = authenticationService;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public Optional<? extends Account> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Account createAccount(UserDto userDto) {
        assertUserInfoIsValid(userDto);
        Role role = roleRepository.findByRole(RoleEnum.COACHEE);
        User userToBeSaved = userMapper.toEntity(userDto, role);
        userToBeSaved.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(userToBeSaved);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getUser(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No such user exists");
        }
        return user.get();
    }

    public UserDto getCoacheeProfileDto(String userId) {

        if (authenticationService.getEmailFromAuthentication().equals("anonymousUser")) {
            return userMapper.toCoacheeProfileDtoWithoutRole(getUser(userId));
        }

        if (authenticationService.hasRole("ADMIN")
                || authenticationService.getAuthenticationIdFromDb().equals(userId)) {
            return userMapper.toCoacheeProfileDto(getUser(userId));
        }
        return userMapper.toCoacheeProfileDtoWithoutRole(getUser(userId));
    }

    public UserDto getCoachProfileDto(String userId) {
        if (getUser(userId).getCoachInformation() == null) {
            return getCoacheeProfileDto(userId);
        }
        return userMapper.toCoachProfileDto(getUser(userId));
    }

    public void becomeCoach(String userId, HttpServletResponse response) {
        User user = getUser(userId);

        if (authenticationService.hasRole("COACHEE")
                && !authenticationService.hasRole("COACH")) {
            authenticationService.assertUserIsChangingOwnProfileOrIsAdmin(userId, FORBIDDEN_ACCESS_MESSAGE);
            user.getRoles().add(roleRepository.findByRole(RoleEnum.COACH));

            CoachInformation coachInformation = new CoachInformation();
            CoachInformation savedCoachInformation = coachInformationService.save(coachInformation);
            user.setCoachInformation(savedCoachInformation);

            String token = jwtGenerator.generateToken(user);

            response.addHeader("Authorization", "Bearer " + token);
            response.addHeader("Access-Control-Expose-Headers", "Authorization");
        }
    }

    public void updateUser(String userId, UserDto userDto) {
        User user = getUser(userId);

        if (authenticationService.hasRole("ADMIN")) {
            if (userDto.getRoles() != null) {
                user.setRoles(roleMapper.toEntity(userDto.getRoles()));
            }
            setRegularUserFields(userDto, user);
        } else if (authenticationService.hasRole("COACHEE")) {
            authenticationService.assertUserIsChangingOwnProfileOrIsAdmin(userId, FORBIDDEN_ACCESS_MESSAGE);
            setRegularUserFields(userDto, user);
        }
    }

    public void updateCoach(String userId, UserDto userDto) {
        authenticationService.assertUserIsChangingOwnProfileOrIsAdmin(userId, FORBIDDEN_ACCESS_MESSAGE);
        User user = getUser(userId);
        setCoachFields(userDto, user);
    }

    public List<UserDto> getAllCoaches() {
        return userMapper.toCoachProfileDto(userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(new Role(RoleEnum.COACH)))
                .collect(Collectors.toList()));
    }

    public List<UserDto> getAllUsers() {
        if (authenticationService.hasRole("ADMIN")) {
            return userMapper.toCoacheeProfileDto(userRepository.findAll());
        }
        throw new WrongRoleException("No go");
    }

    public void updateCoachingTopics(String userId, List<CoachingTopicDto> coachingTopicDtos) {
        authenticationService.assertUserIsChangingOwnProfileOrIsAdmin(userId, FORBIDDEN_ACCESS_MESSAGE);
        assertNotTooManyTopicsAreProvided(coachingTopicDtos);
        assertTopicsAreUnique(coachingTopicDtos);

        List<CoachingTopic> coachingTopics = getCoachingTopicsForUser(userId);
        deleteCoachingTopics(coachingTopics);
        addCoachingTopics(coachingTopicDtos, coachingTopics);


    }

    private void assertUserInfoIsValid(UserDto userDto) {
        memberValidator.validate(userDto);
    }

    private void setRegularUserFields(UserDto userDto, User user) {
        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getPicture() != null) {
            user.setPicture(userDto.getPicture());
        }
    }

    private void setCoachFields(UserDto userDto, User user) {
        CoachInformationDto coachInfo = userDto.getCoachInformation();

        if (coachInfo.getAvailability() != null) {
            user.getCoachInformation().setAvailability(coachInfo.getAvailability());
        }
        if (coachInfo.getIntroduction() != null) {
            user.getCoachInformation().setIntroduction(coachInfo.getIntroduction());
        }
    }

    private void assertNotTooManyTopicsAreProvided(List<CoachingTopicDto> coachingTopicDtos) {
        if(coachingTopicDtos.size() > 2) {
            throw new TopicException("Max 2 topics allowed");
        }
    }

    private void assertTopicsAreUnique(List<CoachingTopicDto> coachingTopicDtos) {
        if(coachingTopicDtos.size() == 2) {
            if(coachingTopicDtos.get(0).getTopic().getName().equals(coachingTopicDtos.get(1).getTopic().getName())) {
                throw new TopicException("A coach cannot teach the same topic twice");
            }
        }
    }

    private List<CoachingTopic> getCoachingTopicsForUser(String userId) {
        User user = getUser(userId);
        return user.getCoachInformation().getCoachingTopics();
    }

    private void deleteCoachingTopics(List<CoachingTopic> coachingTopics) {

        for(CoachingTopic coachingTopic : coachingTopics) {
            deleteCoachingTopic(coachingTopic.getCoachingTopicId());
        }
    }

    private void deleteCoachingTopic(String coachingTopicId) {
        CoachingTopic coachingTopic = coachingTopicRepository.findById(coachingTopicId)
                .orElseThrow(() -> new CoachingTopicException("Coaching topic not found"));
        coachingTopicRepository.delete(coachingTopic);
    }

    private void addCoachingTopics(List<CoachingTopicDto> coachingTopicDtos, List<CoachingTopic> coachingTopics) {
        for(CoachingTopicDto coachingTopicDto : coachingTopicDtos) {
            Topic topic = topicService.findById(coachingTopicDto.getTopic().getName())
                    .orElseThrow(() -> new TopicException("Topic " + coachingTopicDto.getTopic().getName() + " was not found"));

            CoachingTopic coachingTopic = coachingTopicMapper.toEntity(coachingTopicDto);
            coachingTopic.setExperience(coachingTopicDto.getExperience());
            coachingTopic.setTopic(topic);
            coachingTopics.add(coachingTopic);
        }
    }
}
