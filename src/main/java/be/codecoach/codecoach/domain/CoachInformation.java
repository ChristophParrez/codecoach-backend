package be.codecoach.codecoach.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;

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

    @Embedded
    private Collection<CoachingTopic> coachingTopics;

}
