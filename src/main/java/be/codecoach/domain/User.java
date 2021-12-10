package be.codecoach.domain;

import be.codecoach.security.authentication.user.api.Account;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements Account {

    public User() {}

    public User(String firstName, String lastName, String email, String companyName, String password, List<Role> roles, String picture, CoachInformation coachInformation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.companyName = companyName;
        this.password = password;
        this.roles = roles;
        this.picture = picture;
        this.coachInformation = coachInformation;
    }

    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },
    fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @Column(name = "picture")
    private String picture;

    @OneToOne
    @JoinColumn(name = "coach_info", referencedColumnName = "coach_info_id")
    private CoachInformation coachInformation;


    @Override
    public String getId() {
        return this.userId;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String encode) {
        this.password = encode;
    }

    @Override
    public List<RoleEnum> getAuthorities() {
        return this.roles.stream().map(Role::getRole).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountEnabled() {
        return true;
    }

    @Override
    public void enableAccount() {

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getPicture() {
        return picture;
    }
}
