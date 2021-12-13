package be.codecoach.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    public Role() {}

    public Role(RoleEnum role) {
        this.role = role;
    }

    private Role(RoleBuilder roleBuilder){
        this.roleId = roleBuilder.roleId;
        this.role = roleBuilder.role;
        this.users = roleBuilder.users;
    }

    public int getRoleId() {
        return roleId;
    }

    public RoleEnum getRole() {
        return role;
    }

    @Id
    @Column(name = "role_id")
    @SequenceGenerator(name = "role_seq", sequenceName = "ROLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    private int roleId;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @ManyToMany(mappedBy = "roles")
    @Column(name = "users")
    private Set<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return role == role1.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

    public static class RoleBuilder extends Builder<Role> {

        private int roleId;
        private RoleEnum role;
        private Set<User> users;

        private RoleBuilder(){
        }

        @Override
        public Role build() {
            return new Role(this);
        }

        public RoleBuilder withId(int roleId){
            this.roleId = roleId;
            return this;
        }

        public RoleBuilder withRoleEnum(RoleEnum role){
            this.role = role;
            return this;
        }

        public RoleBuilder withUsers(Set<User> users){
            this.users = users;
            return this;
        }
    }
}
