package be.codecoach.builder;

import be.codecoach.domain.Builder;
import be.codecoach.domain.Role;
import be.codecoach.domain.Role.RoleBuilder;
import be.codecoach.domain.RoleEnum;
import be.codecoach.domain.User;

import java.util.Set;

public class RoleTestBuilder extends Builder<Role> {

    private RoleBuilder roleBuilder;

    public RoleTestBuilder(RoleBuilder roleBuilder) {
        this.roleBuilder = roleBuilder;
    }

    public static RoleTestBuilder aRole() {
        return new RoleTestBuilder(RoleBuilder.role()
                .withId(0)
                .withRoleEnum(RoleEnum.COACHEE)
                .withUsers(Set.of(User.UserBuilder.user().build())));
    }

    @Override
    public Role build() {
        return roleBuilder.build();
    }
}
