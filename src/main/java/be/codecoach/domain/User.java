package be.codecoach.domain;

import be.codecoach.security.authentication.user.api.Account;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements Account {

    public User() {}

    public User(String firstName, String lastName, String email, String companyName, String password, Set<Role> roles, String picture, CoachInformation coachInformation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.companyName = companyName;
        this.password = password;
        this.roles = roles;
        this.picture = picture;
        this.coachInformation = coachInformation;
    }

    private User(UserBuilder userBuilder){
        this.userId = userBuilder.userId;
        this.firstName = userBuilder.firstName;
        this.lastName = userBuilder.lastName;
        this.email = userBuilder.email;
        this.companyName = userBuilder.companyName;
        this.password = userBuilder.password;
        this.roles = userBuilder.roles;
        this.picture = userBuilder.picture;
        this.coachInformation = userBuilder.coachInformation;
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
    private Set<Role> roles;

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

    public Set<Role> getRoles() {
        return roles;
    }

    public String getPicture() {
        return picture;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public CoachInformation getCoachInformation() {
        return this.coachInformation;
    }

    public static class UserBuilder extends Builder<User> {

        private String userId;
        private String firstName;
        private String lastName;
        private String email;
        private String companyName;
        private String password;
        private Set<Role> roles;
        private String picture;
        private CoachInformation coachInformation;

        private UserBuilder(){
        }

        @Override
        public User build() {
            return new User(this);
        }

        public UserBuilder withId(String userId){
            this.userId = userId;
            return this;
        }

        public UserBuilder withFirstName(String firstName){
            this.firstName = firstName;
            return this;
        }

        public UserBuilder withLastName(String lastName){
            this.lastName = lastName;
            return this;
        }

        public UserBuilder withEmail(String email){
            this.email = email;
            return this;
        }

        public UserBuilder withCompanyName(String companyName){
            this.companyName = companyName;
            return this;
        }

        public UserBuilder withPassword(String password){
            this.password = password;
            return this;
        }

        public UserBuilder withRoles(Set<Role> roles){
            this.roles = roles;
            return this;
        }

        public UserBuilder withPicture(String picture){
            this.picture = picture;
            return this;
        }

        public UserBuilder withCoachInformation(CoachInformation coachInformation){
            this.coachInformation = coachInformation;
            return this;
        }
    }
}
