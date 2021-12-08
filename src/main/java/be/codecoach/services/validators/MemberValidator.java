package be.codecoach.services.validators;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberValidator{

    private final UserRepository userRepository;

    @Autowired
    public MemberValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(UserDto userDto) {
        assertDtoIsValid(userDto);
        assertEmailIsValid(userDto.getEmail());
        assertEmailIsUnique(userDto.getEmail());
    }

    private void assertDtoIsValid(UserDto userDto) {
        // ToDo
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
