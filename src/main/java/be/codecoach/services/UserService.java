package be.codecoach.services;

import be.codecoach.api.dtos.CoachInformationDto;
import be.codecoach.api.dtos.CoachingTopicDto;
import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.*;
import be.codecoach.exceptions.CoachingTopicException;
import be.codecoach.exceptions.ForbiddenAccessException;
import be.codecoach.exceptions.TopicException;
import be.codecoach.exceptions.UserNotFoundException;
import be.codecoach.repositories.CoachingTopicRepository;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.UserRepository;
import be.codecoach.security.authentication.user.api.Account;
import be.codecoach.security.authentication.user.api.AccountService;
import be.codecoach.services.mappers.CoachingTopicMapper;
import be.codecoach.services.mappers.RoleMapper;
import be.codecoach.services.mappers.UserMapper;
import be.codecoach.services.validators.MemberValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserMapper userMapper, RoleMapper roleMapper, UserRepository userRepository, MemberValidator memberValidator, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CoachingTopicMapper coachingTopicMapper, CoachingTopicRepository coachingTopicRepository, TopicService topicService, CoachInformationService coachInformationService) {
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
    }

    public Account registerUser(UserDto userDto) {
        assertUserInfoIsValid(userDto);
        Role role = roleRepository.findByRole(RoleEnum.COACHEE);
        User userToBeSaved = userMapper.toEntity(userDto, role);
        userToBeSaved.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(userToBeSaved);
    }

    private void assertUserInfoIsValid(UserDto userDto) {
        memberValidator.validate(userDto);
    }

    @Override
    public Optional<? extends Account> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Account createAccount(UserDto dto) {
        return registerUser(dto);
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

    public void becomeCoach(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authorities: " + authentication);

        boolean hasUserRoleCoachee = hasRole(authentication, "COACHEE");
        boolean hasUserRoleCoach = hasRole(authentication, "COACH");

        String emailFromToken = authentication.getName();
        String idFromDatabase = userRepository.findByEmail(emailFromToken).orElseThrow(() -> new NullPointerException("Email from token was not found in the database.")).getId();

        User user = getUser(userId);

        if (hasUserRoleCoachee && !hasUserRoleCoach) {
            if (!userId.equals(idFromDatabase)) {
                throw new ForbiddenAccessException("You cannot change someone else's profile!");
            }
            user.getRoles().add(roleRepository.findByRole(RoleEnum.COACH));

            CoachInformation coachInformation = new CoachInformation();
            CoachInformation savedCoachInformation = coachInformationService.save(coachInformation);
            user.setCoachInformation(savedCoachInformation);
        }

    }

    public void updateUser(String userId, UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authorities: " + authentication);

        boolean hasUserRoleCoachee = hasRole(authentication, "COACHEE");
        boolean hasUserRoleAdmin = hasRole(authentication, "ADMIN");

        String emailFromToken = authentication.getName();
        String idFromDatabase = userRepository.findByEmail(emailFromToken).orElseThrow(() -> new NullPointerException("Email from token was not found in the database.")).getId();

        User user = getUser(userId);

        if (hasUserRoleAdmin) {
            if (userDto.getRoles() != null) {
                user.setRoles(roleMapper.toEntity(userDto.getRoles()));
            }
            setRegularUserFields(userDto, user);
        } else if (hasUserRoleCoachee) {
            if (!userId.equals(idFromDatabase)) {
                throw new ForbiddenAccessException("You cannot change someone else's profile!");
            }
            setRegularUserFields(userDto, user);
        }
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
        List<CoachingTopicDto> coachingTopics = coachInfo.getCoachingTopics();

        if (coachInfo.getAvailability() != null) {
            user.getCoachInformation().setAvailability(coachInfo.getAvailability());
        }
        if (coachInfo.getIntroduction() != null) {
            user.getCoachInformation().setIntroduction(coachInfo.getIntroduction());
        }
        /*if (coachingTopics != null) {
            coachingTopics = coachingTopics.stream().filter(item -> item.getTopic() != null).collect(Collectors.toList());
            user.getCoachInformation().setCoachingTopics(coachingTopicMapper.toEntity(coachingTopics));
        }*/
    }

    public void updateCoach(String userId, UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authorities: " + authentication);

        boolean hasUserRoleCoach = hasRole(authentication, "COACH");

        String emailFromToken = authentication.getName();
        String idFromDatabase = userRepository.findByEmail(emailFromToken).orElseThrow(() -> new NullPointerException("Email from token was not found in the database.")).getId();

        User user = getUser(userId);

        if (hasUserRoleCoach && !userId.equals(idFromDatabase)) {
            throw new ForbiddenAccessException("You cannot change someone else's profile!");
        }
        setCoachFields(userDto, user);
    }

    private boolean hasRole(Authentication authentication, String roleName) {
        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(roleName));
    }

    public void addTopic(String userId, CoachingTopicDto coachingTopicDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authorities: " + authentication);

        boolean hasUserRoleCoach = hasRole(authentication, "COACH");

        String emailFromToken = authentication.getName();
        String idFromDatabase = userRepository.findByEmail(emailFromToken).orElseThrow(() -> new NullPointerException("Email from token was not found in the database.")).getId();

        User user = getUser(userId);

        if (hasUserRoleCoach && !userId.equals(idFromDatabase)) {
            throw new ForbiddenAccessException("You cannot change someone else's profile!");
        }

        Topic topic = topicService.findById(coachingTopicDto.getTopic().getName()).orElseThrow(() -> new TopicException("Topic not found 1"));
        List<CoachingTopic> coachingTopics = user.getCoachInformation().getCoachingTopics();

        if (coachingTopics.size() >= 2) {
            throw new TopicException("Max 2 topics allowed");
        }

        coachingTopics.add(coachingTopicMapper.toEntity(coachingTopicDto));
        CoachingTopic coachingTopic = user.getCoachInformation().getCoachingTopics().stream()
                .filter(top -> top.getTopic().getName().equals(topic.getName())).findFirst().orElseThrow(() -> new TopicException("Topic not found 2"));

        coachingTopic.setExperience(coachingTopicDto.getExperience());
        coachingTopic.setTopic(topic);
    }

    public void deleteCoachingTopic(String userId, String coachingTopicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authorities: " + authentication);

        boolean hasUserRoleCoach = hasRole(authentication, "COACH");

        String emailFromToken = authentication.getName();
        String idFromDatabase = userRepository.findByEmail(emailFromToken).orElseThrow(() -> new NullPointerException("Email from token was not found in the database.")).getId();

        User user = getUser(userId);

        if (hasUserRoleCoach && !userId.equals(idFromDatabase)) {
            throw new ForbiddenAccessException("You cannot change someone else's profile!");
        }

        CoachingTopic coachingTopic = coachingTopicRepository.findById(coachingTopicId).orElseThrow( () -> new CoachingTopicException("Coaching topic not found"));

        coachingTopicRepository.delete(coachingTopic);
    }

        /*public void updateCoachingTopics(String userId, CoachingTopicDto coachingTopicDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authorities: " + authentication);

        boolean hasUserRoleCoach = hasRole(authentication, "COACH");

        String emailFromToken = authentication.getName();
        String idFromDatabase = userRepository.findByEmail(emailFromToken).orElseThrow(() -> new NullPointerException("Email from token was not found in the database.")).getId();

        User user = getUser(userId);

        if (hasUserRoleCoach && !userId.equals(idFromDatabase)) {
            throw new ForbiddenAccessException("You cannot change someone else's profile!");
        }

        List<CoachingTopic> coachingTopics = user.getCoachInformation().getCoachingTopics();

        for (int i = 0; i < coachingTopics.size(); i++) {
            if (coachingTopics.get(i).getCoachingTopicId().equals(coachingTopicDto.getCoachingTopicId())) {
                coachingTopics.get(i).setTopic(coachingTopicMapper.toEntity(coachingTopicDto).getTopic());
                coachingTopics.get(i).setExperience(coachingTopicMapper.toEntity(coachingTopicDto).getExperience());
                break;
            }
        }
    }*/
}
