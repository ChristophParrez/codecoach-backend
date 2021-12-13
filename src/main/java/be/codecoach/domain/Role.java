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
}
