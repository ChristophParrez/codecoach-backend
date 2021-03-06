package be.codecoach.services;

import be.codecoach.api.dtos.CoachInformationDto;
import be.codecoach.api.dtos.CoachingTopicDto;
import be.codecoach.api.dtos.RoleDto;
import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.*;
import be.codecoach.exceptions.NotUniqueException;
import be.codecoach.exceptions.UserNotFoundException;
import be.codecoach.exceptions.WrongRoleException;
import be.codecoach.repositories.UserRepository;
import be.codecoach.security.authentication.jwt.JwtGenerator;
import be.codecoach.security.authentication.user.api.Account;
import be.codecoach.security.authentication.user.api.AccountService;
import be.codecoach.services.mappers.UserMapper;
import be.codecoach.services.validators.MemberValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements AccountService {

    private static final String FORBIDDEN_ACCESS_MESSAGE = "You cannot change someone else's profile!";
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserMapper userMapper;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final MemberValidator memberValidator;
    private final PasswordEncoder passwordEncoder;
    private final CoachingTopicService coachingTopicService;
    private final CoachInformationService coachInformationService;
    private final AuthenticationService authenticationService;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public UserService(UserMapper userMapper, RoleService roleService, UserRepository userRepository, MemberValidator memberValidator, PasswordEncoder passwordEncoder, CoachingTopicService coachingTopicService, CoachInformationService coachInformationService, AuthenticationService authenticationService, JwtGenerator jwtGenerator) {
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.memberValidator = memberValidator;
        this.passwordEncoder = passwordEncoder;
        this.coachingTopicService = coachingTopicService;
        this.coachInformationService = coachInformationService;
        this.authenticationService = authenticationService;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public Optional<? extends Account> findByEmail(String email) {
        logger.info("Looking up User with Email address: " + email);
        return userRepository.findByEmail(email);
    }

    @Override
    public Account createAccount(UserDto userDto) {
        memberValidator.validate(userDto);
        Role role = roleService.findByRole(RoleEnum.COACHEE);
        User userToBeSaved = userMapper.toEntity(userDto, role);
        userToBeSaved.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userToBeSaved.setEmail(userToBeSaved.getEmail().toLowerCase());
        return userRepository.save(userToBeSaved);
    }

    @Override
    public boolean existsByEmail(String email) {
        logger.info("Checking if email address " + email + " is registered");
        return userRepository.existsByEmail(email);
    }

    public User getUser(String userId) {
        logger.info("Looking up a user by id " + userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("No such user exists");
        }
        logger.info("User with id " + userId + " found");
        return user.get();
    }

    public UserDto getCoacheeProfileDto(String userId) {

        if (authenticationService.getEmailFromAuthentication() == null || authenticationService.getEmailFromAuthentication().equals("anonymousUser")) {
            logger.info("User is unauthenticated. Returning COACHEE profile without role.");
            return userMapper.toCoacheeProfileDtoWithoutRole(getUser(userId));
        }

        if (authenticationService.hasRole("ADMIN")
                || authenticationService.getAuthenticationIdFromDb().equals(userId)) {
            logger.info("User is ADMIN or accessing own profile. Returning COACHEE profile with role.");
            return userMapper.toCoacheeProfileDto(getUser(userId));
        }
        logger.info("User is not ADMIN nor accessing own profile. Returning COACHEE profile without role");
        return userMapper.toCoacheeProfileDtoWithoutRole(getUser(userId));
    }

    public UserDto getCoachProfileDto(String userId) {
        if (getUser(userId).getCoachInformation() == null) {
            logger.info("User with id " + userId + " is not a COACH. Returning COACHEE profile");
            return getCoacheeProfileDto(userId);
        }
        return userMapper.toCoachProfileDto(getUser(userId));
    }

    public void becomeCoach(String userId, HttpServletResponse response) {
        User user = getUser(userId);

        if (authenticationService.hasRole("COACHEE")
                && !authenticationService.hasRole("COACH")) {
            authenticationService.assertUserHasRightsToPerformAction(userId, FORBIDDEN_ACCESS_MESSAGE);
            user.getRoles().add(roleService.findByRole(RoleEnum.COACH));

            user.setCoachInformation(coachInformationService.save(new CoachInformation()));
            updateToken(user, response);
            logger.info("COACH role added for user " + userId);
        }
    }

    public void updateUser(String userId, UserDto userDto, HttpServletResponse response) {
        User user = getUser(userId);

        if (authenticationService.hasRole("ADMIN")) {
            if (userDto.getRoles() != null) {
                Set<Role> roles = new HashSet<>();
                for (RoleDto roleDto : userDto.getRoles()) {
                    roles.add(roleService.findByRole(roleDto.getRole()));
                }
                user.setRoles(roles);
            }
            if (userId.equals(authenticationService.getAuthenticationIdFromDb())) {
                updateToken(user, response);
            }
            setRegularUserFields(userDto, user);
            logger.info("Profile updated by ADMIN");
        } else if (authenticationService.hasRole("COACHEE")) {
            authenticationService.assertUserHasRightsToPerformAction(userId, FORBIDDEN_ACCESS_MESSAGE);
            setRegularUserFields(userDto, user);
            logger.info("Profile updated by user " + userId);
        }
    }

    public void updateCoach(String userId, UserDto userDto) {
        authenticationService.assertUserHasRightsToPerformAction(userId, FORBIDDEN_ACCESS_MESSAGE);
        User user = getUser(userId);
        setCoachFields(userDto, user);
        logger.info("COACH profile updated for user " + userId);
    }

    public List<UserDto> getAllCoaches() {
        return userMapper.toCoachProfileDto(userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(new Role(RoleEnum.COACH)))
                .collect(Collectors.toList()));
    }

    public List<UserDto> getAllUsers() {
        if (authenticationService.hasRole("ADMIN")) {
            logger.info("Returning all user profiles requested by ADMIN");
            return userMapper.toCoacheeProfileDto(userRepository.findAll());
        }
        throw new WrongRoleException("No go");
    }

    public void updateCoachingTopics(String userId, List<CoachingTopicDto> coachingTopicDtos) {
        authenticationService.assertUserHasRightsToPerformAction(userId, FORBIDDEN_ACCESS_MESSAGE);
        coachingTopicService.assertInputIsValid(coachingTopicDtos);

        List<CoachingTopic> coachingTopics = getCoachingTopicsForUser(userId);

        coachingTopicService.deleteCoachingTopicsFromDb(coachingTopics);

        coachingTopics = coachingTopicService.addCoachingTopics(coachingTopicDtos);

        getUser(userId).getCoachInformation().setCoachingTopics(coachingTopics);
    }

    private void setRegularUserFields(UserDto userDto, User user) {
        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            if (userRepository.existsByEmail(userDto.getEmail().toLowerCase()) && !user.getEmail().equals(userDto.getEmail())) {
                throw new NotUniqueException("This email address is already used!");
            }
            user.setEmail(userDto.getEmail().toLowerCase());
        }
        if (userDto.getPicture() != null) {
            user.setPicture(userDto.getPicture());
        }
        if (userDto.getTelephoneNumber() != null) {
            user.setTelephoneNumber(userDto.getTelephoneNumber());
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

    private List<CoachingTopic> getCoachingTopicsForUser(String userId) {
        User user = getUser(userId);
        return user.getCoachInformation().getCoachingTopics();
    }

    private void updateToken(User user, HttpServletResponse response) {
        String token = jwtGenerator.generateToken(user);

        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
    }
}
