package be.codecoach.services;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.User;
import be.codecoach.exceptions.ForbiddenAccessException;
import be.codecoach.exceptions.UserNotFoundException;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.UserRepository;
import be.codecoach.security.authentication.user.api.Account;
import be.codecoach.security.authentication.user.api.AccountService;
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

import java.util.Optional;
import java.util.Set;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserMapper userMapper, RoleMapper roleMapper, UserRepository userRepository, MemberValidator memberValidator, RoleRepository roleRepository, PasswordEncoder passwordEncoder/*, SecuredUserService securedUserService*/) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRepository = userRepository;
        this.memberValidator = memberValidator;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
        return Optional.ofNullable(userRepository.findByEmail(email));
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
        if(user.isEmpty()) {
            throw new UserNotFoundException("No such user exists");
        }
        return user.get();
    }

    public UserDto getCoacheeProfileDto(String userId) {
        return userMapper.toCoacheeProfileDto(getUser(userId));
    }

    public UserDto getCoachProfileDto(String userId) {
        return userMapper.toCoachProfileDto(getUser(userId));
    }

    public void becomeCoach(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authorities: " + authentication);

        boolean hasUserRoleCoachee = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("COACHEE"));

        String emailFromToken = authentication.getName();
        String idFromDatabase = userRepository.findByEmail(emailFromToken).orElseThrow(() -> new NullPointerException("Email from token was not found in the database.")).getId();

        User user = getUser(userId);

        if (hasUserRoleCoachee) {
            if(!userId.equals(idFromDatabase)) {
                throw new ForbiddenAccessException("You cannot change someone else's profile!");
            }
            user.getRoles().add(roleRepository.findByRole(RoleEnum.COACH));
        }

    }

    public void updateUser(String userId, UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authorities: " + authentication);

        boolean hasUserRoleCoachee = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("COACHEE"));
        boolean hasUserRoleAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));

        String emailFromToken = authentication.getName();
        String idFromDatabase = userRepository.findByEmail(emailFromToken).orElseThrow(() -> new NullPointerException("Email from token was not found in the database.")).getId();

        User user = getUser(userId);
        if(hasUserRoleCoachee){
            setUserFields(userDto, user);
        }
        if(hasUserRoleAdmin){
            if (userDto.getRoles() != null) {
                user.setRoles(roleMapper.toEntity(userDto.getRoles()));
            }
            setUserFields(userDto, user);
        } else if (hasUserRoleCoachee) {
            if(!userId.equals(idFromDatabase)) {
                throw new ForbiddenAccessException("You cannot change someone else's profile!");
            }
            setUserFields(userDto, user);
        }
    }

    private void setUserFields(UserDto userDto, User user) {
        if (userDto.getFirstName() != null){
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null){
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null){
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getPicture() != null){
            user.setPicture(userDto.getPicture());
        }
    }



}
