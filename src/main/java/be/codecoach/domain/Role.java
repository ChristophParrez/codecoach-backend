package be.codecoach.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    public Role() {}

    public Role(RoleEnum role) {
        this.role = role;
    }

    public String getRoleId() {
        return roleId;
    }

    public RoleEnum getRole() {
        return role;
    }

    @Id
    @Column(name = "role_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String roleId;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
