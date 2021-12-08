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

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public void registerUser(UserDto userDto) {
        assertUserInfoIsValid(userDto);

        userRepository.save(userMapper.toEntity(userDto));
    }

    private void assertUserInfoIsValid(UserDto userDto) {
        assertDtoIsValid(userDto);
        assertEmailIsValid(userDto.getEmail());
        assertEmailIsUnique(userDto.getEmail()); // => userRepository.existsByEmail
    }

    private void assertDtoIsValid(UserDto userDto) {
        MemberValidator memberValidator = new MemberValidator();
        memberValidator.validate(userDto);
    }


    private void assertEmailIsValid(String email) {
        if (email == null) {
            throw new IllegalArgumentException();
        }

        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);

        if(!m.matches()) {
            throw new IllegalArgumentException();
        }
    }

    private void assertEmailIsUnique(String email) {
        if(userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException();
        }
    }
}
