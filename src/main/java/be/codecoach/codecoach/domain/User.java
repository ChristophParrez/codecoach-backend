package be.codecoach.codecoach.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User {


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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "role_id")
    private Collection<Role> roles;

    @Column(name = "picture")
    private String picture;

    @OneToOne
    @JoinColumn(name = "coach_info", referencedColumnName = "coach_info_id")
    private CoachInformation coachInformation;

}
