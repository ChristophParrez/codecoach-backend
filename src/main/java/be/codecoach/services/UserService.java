package be.codecoach.services;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.User;
import be.codecoach.exceptions.UserNotFoundException;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.UserRepository;
import be.codecoach.security.authentication.user.api.Account;
import be.codecoach.security.authentication.user.api.AccountService;
import be.codecoach.services.mappers.RoleMapper;
import be.codecoach.services.mappers.UserMapper;
import be.codecoach.services.validators.MemberValidator;
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
    //private final SecuredUserService securedUserService;

    @Autowired
    public UserService(UserMapper userMapper, RoleMapper roleMapper, UserRepository userRepository, MemberValidator memberValidator, RoleRepository roleRepository, PasswordEncoder passwordEncoder/*, SecuredUserService securedUserService*/) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRepository = userRepository;
        this.memberValidator = memberValidator;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        //this.securedUserService = securedUserService;
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

    public void updateUser(String userId, UserDto userDto) {
        //trying to get the roles from the token...
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authorities: " + authentication);
        Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("authorities Baeldung: " + currentPrincipalName);
        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());
        System.out.println("roles; " + roles);

        boolean hasUserRoleCoachee = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("COACHEE"));
        boolean hasUserRoleAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));

        User user = getUser(userId);
        if(hasUserRoleCoachee){
            setUserFields(userDto, user);
        }
        if(hasUserRoleAdmin){
            user.setRoles(roleMapper.toEntity(userDto.getRoles()));
            setUserFields(userDto, user);
        }

        //System.out.println(securedUserService.loadUserByUsername(userDto.getEmail()));

    }

    private void setUserFields(UserDto userDto, User user) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPicture(userDto.getPicture());
    }


}
