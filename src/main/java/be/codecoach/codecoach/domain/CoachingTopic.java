package be.codecoach.codecoach.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "coaching_topics")
public class CoachingTopic {

    @Id
    @Column(name = "coaching_topic_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String coachingTopicId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "topic", referencedColumnName = "topic_id")
    private Topic topic;

    @Column(name = "experience")
    private double experience;

    @ManyToOne
    @JoinColumn(name = "coach_info_id")
    private CoachInformation coachInformation;

}
