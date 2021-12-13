package be.codecoach.services.validators;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.exceptions.InvalidEmailException;
import be.codecoach.exceptions.InvalidPasswordException;
import be.codecoach.exceptions.NoInputException;
import be.codecoach.exceptions.NotUniqueException;
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
        assertFieldsNotNull(userDto);
        assertEmailIsValid(userDto.getEmail());
        assertEmailIsUnique(userDto.getEmail());
        assertPasswordValid(userDto.getPassword());
    }

    private void assertFieldsNotNull(UserDto userDto) {
        if(inputEmpty(userDto.getFirstName())) {
            throw new NoInputException("First Name can not be null");
        }
        if(inputEmpty(userDto.getLastName())) {
            throw new NoInputException("Last Name can not be null");
        }
        if(inputEmpty(userDto.getEmail())) {
            throw new NoInputException("Email can not be null");
        }
        if(inputEmpty(userDto.getPassword())) {
            throw new NoInputException("Password can not be null");
        }if(inputEmpty(userDto.getCompanyName())) {
            throw new NoInputException("Company/team can not be null");
        }

    }

    private boolean inputEmpty(String input) {
        return (input == null || input.isEmpty() || input.isBlank());
    }


    private void assertEmailIsValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);

        if(!m.matches()) {
            throw new InvalidEmailException("Email is invalid");
        }
    }

    private void assertEmailIsUnique(String email) {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new NotUniqueException("Email already exists!");
        }
    }

    private void assertPasswordValid(String password) {
        String pattern = "(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{8,}";
        if(!password.matches(pattern)){
            throw new InvalidPasswordException("Password syntax must match input requirements");
        }
    }
}
