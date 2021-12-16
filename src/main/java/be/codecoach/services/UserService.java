package be.codecoach.services;

import be.codecoach.api.dtos.CoachInformationDto;
import be.codecoach.api.dtos.CoachingTopicDto;
import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.*;
import be.codecoach.exceptions.CoachingTopicException;
import be.codecoach.exceptions.TopicException;
import be.codecoach.exceptions.UserNotFoundException;
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

    private void assertUserInfoIsValid(UserDto userDto) {
        memberValidator.validate(userDto);
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
        return userMapper.toCoacheeProfileDto(getUser(userId));
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
            authenticationService.assertUserIsChangingOwnProfile(userId);
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
            authenticationService.assertUserIsChangingOwnProfile(userId);
            setRegularUserFields(userDto, user);
        }
    }

    public void addCoachingTopic(String userId, CoachingTopicDto coachingTopicDto) {
        authenticationService.assertUserIsChangingOwnProfile(userId);
        Topic topic = topicService.findById(coachingTopicDto.getTopic().getName())
                .orElseThrow(() -> new TopicException("Topic not found"));
        User user = getUser(userId);
        List<CoachingTopic> coachingTopics = user.getCoachInformation().getCoachingTopics();

        if (coachingTopics.size() >= 2) {
            throw new TopicException("Max 2 topics allowed");
        }

        CoachingTopic coachingTopic = coachingTopicMapper.toEntity(coachingTopicDto);
        coachingTopic.setExperience(coachingTopicDto.getExperience());
        coachingTopic.setTopic(topic);
        coachingTopics.add(coachingTopic);
    }

    public void deleteCoachingTopic(String userId, String coachingTopicId) {
        authenticationService.assertUserIsChangingOwnProfile(userId);
        CoachingTopic coachingTopic = coachingTopicRepository.findById(coachingTopicId)
                .orElseThrow(() -> new CoachingTopicException("Coaching topic not found"));
        coachingTopicRepository.delete(coachingTopic);
    }

    public void updateCoach(String userId, UserDto userDto) {
        authenticationService.assertUserIsChangingOwnProfile(userId);
        User user = getUser(userId);
        setCoachFields(userDto, user);
    }

    public List<UserDto> getAllCoaches() {
        return userMapper.toCoachProfileDto(userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(new Role(RoleEnum.COACH)))
                .collect(Collectors.toList()));
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
}
