package be.codecoach.codecoach.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;


@Entity
@Table(name = "coach_info")
public class CoachInformation {

    @Id
    @Column(name = "coach_info_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String coachInfoId;

    @Column(name = "coach_xp")
    private int coachXp;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "availability")
    private String availability;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<CoachingTopic> coachingTopics;

}
