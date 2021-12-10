package be.codecoach.services;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.User;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.UserRepository;
import be.codecoach.security.authentication.user.api.Account;
import be.codecoach.security.authentication.user.api.AccountService;
import be.codecoach.services.mappers.UserMapper;
import be.codecoach.services.validators.MemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional //added by Free while doing tests with security
public class UserService implements AccountService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final MemberValidator memberValidator;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder; //added by Free while doing tests with security

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, MemberValidator memberValidator, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.memberValidator = memberValidator;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;//added by Free while doing tests with security
    }

    public Account registerUser(UserDto userDto) {
        assertUserInfoIsValid(userDto);
        Role role = roleRepository.findByRole(RoleEnum.COACHEE);
        User userToBeSaved = userMapper.toEntity(userDto, role);//added by Free while doing tests with security
        userToBeSaved.setPassword(passwordEncoder.encode(userDto.getPassword())); //remove comment to use encoding //added by Free while doing tests with security
        return userRepository.save(userToBeSaved);
        //added by Free while doing tests with security
    }

    private void assertUserInfoIsValid(UserDto userDto) {
        memberValidator.validate(userDto);
    }

    @Override
    public Optional<? extends Account> findByEmail(String email) {
        return Optional.of(userRepository.findByEmail(email));
    }

    @Override
    public Account createAccount(UserDto dto) {
        return registerUser(dto);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
