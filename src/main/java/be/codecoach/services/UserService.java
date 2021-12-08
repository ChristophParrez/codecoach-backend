package be.codecoach.services;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.repositories.UserRepository;
import be.codecoach.services.mappers.UserMapper;
import be.codecoach.services.validators.MemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final MemberValidator memberValidator;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, MemberValidator memberValidator, RoleRepository roleRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.memberValidator = memberValidator;
        this.roleRepository = roleRepository;
    }

    public void registerUser(UserDto userDto) {
        assertUserInfoIsValid(userDto);
        Role role = roleRepository.findByRole(RoleEnum.COACHEE);
        userRepository.save(userMapper.toEntity(userDto, role));
    }

    private void assertUserInfoIsValid(UserDto userDto) {
        memberValidator.validate(userDto);
    }
}
