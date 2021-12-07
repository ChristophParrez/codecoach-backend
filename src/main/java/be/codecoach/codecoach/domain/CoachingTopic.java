package be.codecoach.codecoach.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Embeddable
public class CoachingTopic {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String coachingTopicId;

    @ManyToOne
    @JoinColumn(name = "topic", referencedColumnName = "topic_id")
    private Topic topic;

    @Column(name = "experience")
    private double experience;
}
