package be.codecoach.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @Column(name = "feedback_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @ManyToOne
    @JoinColumn(name = "feedback_giver", referencedColumnName = "user_id")
    private User feedbackGiver;

    @Column(name = "score_one")
    private double score1;

    @Column(name = "score_two")
    private double score2;

    @Column(name = "comment")
    private String comment;
}
