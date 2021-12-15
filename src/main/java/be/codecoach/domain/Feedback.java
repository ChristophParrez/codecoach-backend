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

    public Feedback() {
    }

    public Feedback(User feedbackGiver, double score1, double score2, String comment) {
        this.feedbackGiver = feedbackGiver;
        this.score1 = score1;
        this.score2 = score2;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public User getFeedbackGiver() {
        return feedbackGiver;
    }

    public double getScore1() {
        return score1;
    }

    public double getScore2() {
        return score2;
    }

    public String getComment() {
        return comment;
    }
}
