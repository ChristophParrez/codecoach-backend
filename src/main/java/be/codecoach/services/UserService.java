package be.codecoach.services;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.repositories.UserRepository;
import be.codecoach.services.mappers.UserMapper;
import be.codecoach.services.validators.MemberValidator;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final MemberValidator memberValidator;

    public UserService(UserMapper userMapper, UserRepository userRepository, MemberValidator memberValidator) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.memberValidator = memberValidator;
    }

    public void registerUser(UserDto userDto) {
        assertUserInfoIsValid(userDto);

        userRepository.save(userMapper.toEntity(userDto));
    }

    private void assertUserInfoIsValid(UserDto userDto) {
        memberValidator.validate(userDto);
    }
}
