package be.codecoach.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "sessions")
@Entity
public class Session {

    @Id
    @Column(name = "session_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @ManyToOne
    @JoinColumn(name = "coach", referencedColumnName = "user_id")
    private User coach;

    @ManyToOne
    @JoinColumn(name = "coachee", referencedColumnName = "user_id")
    private User coachee;

    @Column(name = "subject")
    private String subject;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "Location", referencedColumnName = "name")
    private Location location;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "status_name")
    private Status status;

    @OneToOne
    @JoinColumn(name = "coach_feedback", referencedColumnName = "feedback_id")
    private Feedback coachFeedback;

    @OneToOne
    @JoinColumn(name = "coachee_feedback", referencedColumnName = "feedback_id")
    private Feedback coacheeFeedback;
}
