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

    public Session() {
    }

    public Session(User coach, User coachee, String subject, LocalDate date, LocalTime time, Location location, String remarks, Status status, Feedback coachFeedback, Feedback coacheeFeedback) {
        this.coach = coach;
        this.coachee = coachee;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.location = location;
        this.remarks = remarks;
        this.status = status;
        this.coachFeedback = coachFeedback;
        this.coacheeFeedback = coacheeFeedback;
    }

    public String getId() {
        return id;
    }

    public User getCoach() {
        return coach;
    }

    public User getCoachee() {
        return coachee;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Location getLocation() {
        return location;
    }

    public String getRemarks() {
        return remarks;
    }

    public Status getStatus() {
        return status;
    }

    public Feedback getCoachFeedback() {
        return coachFeedback;
    }

    public Feedback getCoacheeFeedback() {
        return coacheeFeedback;
    }
}
