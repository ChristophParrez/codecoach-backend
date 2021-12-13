package be.codecoach.builder;

import be.codecoach.domain.Builder;
import be.codecoach.domain.User;
import be.codecoach.domain.User.UserBuilder;

import java.util.Set;

import static be.codecoach.builder.CoachInformationTestBuilder.aCoachInformation;
import static be.codecoach.builder.RoleTestBuilder.aRole;

public class UserTestBuilder extends Builder<User> {

    private UserBuilder userBuilder;

    public UserTestBuilder(UserBuilder userBuilder) {
        this.userBuilder = userBuilder;
    }

    public static UserTestBuilder anEmptyUser() {
        return new UserTestBuilder(UserBuilder.user());
    }

    public static UserTestBuilder anUser() {
        return new UserTestBuilder(UserBuilder.user()
                .withFirstName("Mert")
                .withLastName("Demirok")
                .withEmail("mertdemirok@gmail.com")
                .withCompanyName("FOD ECO")
                .withPassword("Password123")
                .withRoles(Set.of(aRole().build()))
                .withCoachInformation(aCoachInformation().build()));
    }

    @Override
    public User build() {
        return userBuilder.build();
    }


}
