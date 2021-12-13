package be.codecoach.services.validators;

import be.codecoach.api.dtos.UserDto;
import be.codecoach.domain.User;
import be.codecoach.exceptions.InvalidEmailException;
import be.codecoach.exceptions.InvalidPasswordException;
import be.codecoach.exceptions.NoInputException;
import be.codecoach.exceptions.NotUniqueException;
import be.codecoach.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Testing the MemberValidator methods")
class MemberValidatorTest {

    String firstname;
    String lastname;
    String email;
    String password;
    String company;
    UserDto userDto;
    MemberValidator memberValidator;
    UserRepository userRepositoryMock;

    @BeforeEach
    void setUp() {
        firstname = "John";
        lastname = "Doe";
        email = "John@Doe.com";
        password = "SomePass11!";
        company = "JohnDoeInc";

        userRepositoryMock = Mockito.mock(UserRepository.class);

        memberValidator = new MemberValidator(userRepositoryMock);
    }

    @Test
    @DisplayName("When email already exists, throw NotUniqueException")
    void givenRepositoryWithSpecificEmail_whenValidationDtoWithThatSpecificEmail_thenThrowNotUniqueException() {
        //given
        Mockito.when(userRepositoryMock.findByEmail("code@coach.com")).thenReturn(
                Optional.of(new User()));
        userDto = UserDto.Builder.anUserDto()
                .withFirstName(firstname)
                .withLastName(lastname)
                .withEmail("code@coach.com")
                .withPassword(password)
                .withCompanyName(company)
                .build();

        //when, then
        assertThrows(NotUniqueException.class, () -> memberValidator.validate(userDto));
    }

    @Test
    @DisplayName("Assert Email is Valid")
    void givenValidDtoWithValidEmail_whenAssertEmailIsValid_thenExpectNoExceptions() {
        //given
        userDto = UserDto.Builder.anUserDto()
                .withFirstName(firstname)
                .withLastName(lastname)
                .withEmail(email)
                .withPassword(password)
                .withCompanyName(company)
                .build();

        //when, then
        assertDoesNotThrow(() -> memberValidator.validate(userDto));
    }

    //@Disabled("What is a good (regex) validation for email? What does the currently used regex do?")
    @ParameterizedTest
    @ValueSource(strings = { "noAt.com", "short@extension.c"  })
    @DisplayName("Assert Email is Valid: when not, throw exception")
    void givenDtoWithNonValidEmail_whenAssertEmailIsValid_thenExpectInvalidMailException(String wrongEmail) {
        //given
        userDto = UserDto.Builder.anUserDto()
                .withFirstName(firstname)
                .withLastName(lastname)
                .withEmail(wrongEmail)
                .withPassword(password)
                .withCompanyName(company)
                .build();

        //when, then
        assertThrows(InvalidEmailException.class, () -> memberValidator.validate(userDto));
    }

    @ParameterizedTest
    @ValueSource(strings = { "password12", "PASSWORD", "PASS word12", "Pasw0rd" })
    //1: no capital, 2: no numbers, 3: whitespace, 4: less than 8 characters
    @DisplayName("Assert Password is Valid: when not, throw exception")
    void givenDtoWithNonValidPassword_whenAssertPasswordIsValid_thenExpectInvalidPasswordException(String wrongPassword) {
        //given
        userDto = UserDto.Builder.anUserDto()
                .withFirstName(firstname)
                .withLastName(lastname)
                .withEmail(email)
                .withPassword(wrongPassword)
                .withCompanyName(company)
                .build();

        //when, then
        assertThrows(InvalidPasswordException.class, () -> memberValidator.validate(userDto));
    }

    @Test
    @DisplayName("Assert Password is Valid")
    void givenValidDtoWithValidPassword_whenAssertPasswordIsValid_thenExpectNoExceptions() {
        //given
        userDto = UserDto.Builder.anUserDto()
                .withFirstName(firstname)
                .withLastName(lastname)
                .withEmail(email)
                .withPassword(password)
                .withCompanyName(company)
                .build();

        //when, then
        assertDoesNotThrow(() -> memberValidator.validate(userDto));
    }

    @Nested
    @DisplayName("Fields Not Null assertions")
    class assertFieldsNotNull {

        @Test
        @DisplayName("Assert Fields Not Null, when not null = no exceptions")
        void givenUserDtoWithFirstNameAndAllFieldsFilled_whenAssertFieldsNotNull_thenExpectNoExceptions() {
            //given
            userDto = UserDto.Builder.anUserDto()
                    .withFirstName(firstname)
                    .withLastName(lastname)
                    .withEmail(email)
                    .withPassword(password)
                    .withCompanyName(company)
                    .build();

            //when, then
            assertDoesNotThrow(() -> memberValidator.validate(userDto));
        }

        @Test
        @DisplayName("Assert company/team not null, when null throw error")
        void givenDtoWithNoCompany_whenAssertFieldsNotNull_thenThrowNoInputException() {
            //given
            userDto = UserDto.Builder.anUserDto()
                    .withFirstName(firstname)
                    .withLastName(lastname)
                    .withEmail(email)
                    .withPassword(password)
                    .build();

            //when, then
            assertThrows(NoInputException.class, () -> memberValidator.validate(userDto));
        }

        @Test
        @DisplayName("Assert First Name not null, when null throw error")
        void givenDtoWithNoFirstName_whenAssertFieldsNotNull_thenThrowNoInputException() {
            //given
            userDto = UserDto.Builder.anUserDto()
                    .withLastName(lastname)
                    .withEmail(email)
                    .withPassword(password)
                    .withCompanyName(company)
                    .build();

            //when, then
            assertThrows(NoInputException.class, () -> memberValidator.validate(userDto));
        }

        @Test
        @DisplayName("Assert Last Name not null, when null throw error")
        void givenDtoWithNoLastName_whenAssertFieldsNotNull_thenThrowNoInputException() {
            //given
            userDto = UserDto.Builder.anUserDto()
                    .withFirstName(firstname)
                    .withEmail(email)
                    .withPassword(password)
                    .withCompanyName(company)
                    .build();
            //when, then
            assertThrows(NoInputException.class, () -> memberValidator.validate(userDto));
        }

        @Test
        @DisplayName("Assert Email not null, when null throw error")
        void givenDtoWithNoEmail_whenAssertFieldsNotNull_thenThrowNoInputException() {
            //given
            userDto = UserDto.Builder.anUserDto()
                    .withFirstName(firstname)
                    .withLastName(lastname)
                    .withPassword(password)
                    .withCompanyName(company)
                    .build();
            //when, then
            assertThrows(NoInputException.class, () -> memberValidator.validate(userDto));
        }

        @Test
        @DisplayName("Assert Password not null, when null throw error")
        void givenDtoWithNoPassword_whenAssertFieldsNotNull_thenThrowNoInputException() {
            //given
            userDto = UserDto.Builder.anUserDto()
                    .withFirstName(firstname)
                    .withLastName(lastname)
                    .withEmail(email)
                    .withCompanyName(company)
                    .build();

            //when, then
            assertThrows(NoInputException.class, () -> memberValidator.validate(userDto));
        }

        @Nested
        @DisplayName("inputEmpty method")
        class inputEmptyTest {

            @Test
            @DisplayName("A tested field is null")
            void givenDtoWithANullField_whenValidating_ExpectExceptionToBeThrown() {
                //given
                userDto = UserDto.Builder.anUserDto()
                        .withFirstName(null)
                        .withLastName(lastname)
                        .withEmail(email)
                        .withPassword(password)
                        .withCompanyName(company)
                        .build();

                //when, then
                assertThrows(NoInputException.class, () -> memberValidator.validate(userDto));
            }

            @Test
            @DisplayName("A tested field is empty")
            void givenDtoWithAnEmptyField_whenValidating_ExpectExceptionToBeThrown() {
                //given
                userDto = UserDto.Builder.anUserDto()
                        .withFirstName("")
                        .withLastName(lastname)
                        .withEmail(email)
                        .withPassword(password)
                        .withCompanyName(company)
                        .build();

                //when, then
                assertThrows(NoInputException.class, () -> memberValidator.validate(userDto));
            }

            @Test
            @DisplayName("A tested field is blank")
            void givenDtoWithABlankField_whenValidating_ExpectExceptionToBeThrown() {
                //given
                userDto = UserDto.Builder.anUserDto()
                        .withFirstName(" ")
                        .withLastName(lastname)
                        .withEmail(email)
                        .withPassword(password)
                        .withCompanyName(company)
                        .build();
                //when, then
                assertThrows(NoInputException.class, () -> memberValidator.validate(userDto));
            }

        }

    }


}