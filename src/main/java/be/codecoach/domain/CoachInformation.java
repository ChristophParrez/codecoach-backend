package be.codecoach.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "coach_info")
public class CoachInformation {

    public CoachInformation() {}

    public CoachInformation(int coachXp, String introduction, String availability, List<CoachingTopic> coachingTopics) {
        this.coachXp = coachXp;
        this.introduction = introduction;
        this.availability = availability;
        this.coachingTopics = coachingTopics;
    }

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
    @JoinColumn(name = "coaching_topics")
    private List<CoachingTopic> coachingTopics;

    public int getCoachXp() {
        return coachXp;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getAvailability() {
        return availability;
    }

    public List<CoachingTopic> getCoachingTopics() {
        return coachingTopics;
    }
}
