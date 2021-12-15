package be.codecoach.domain;


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

    public CoachingTopic() {
    }

    public CoachingTopic(Topic topic, double experience) {
        this.topic = topic;
        this.experience = experience;
    }

    private CoachingTopic(CoachingTopicBuilder coachingTopicBuilder) {
        this.coachingTopicId = coachingTopicBuilder.coachingTopicId;
        this.topic = coachingTopicBuilder.topic;
        this.experience = coachingTopicBuilder.experience;
    }

    public String getCoachingTopicId() {
        return coachingTopicId;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public Topic getTopic() {
        return topic;
    }

    public double getExperience() {
        return experience;
    }

    public static class CoachingTopicBuilder extends Builder<CoachingTopic> {

        private String coachingTopicId;
        private Topic topic;
        private double experience;

        private CoachingTopicBuilder() {
        }

        public static CoachingTopicBuilder coachingTopic() {
            return new CoachingTopicBuilder();
        }

        @Override
        public CoachingTopic build() {
            return new CoachingTopic(this);
        }

        public CoachingTopicBuilder withCoachingTopicId(String coachingTopicId) {
            this.coachingTopicId = coachingTopicId;
            return this;
        }

        public CoachingTopicBuilder withTopic(Topic topic) {
            this.topic = topic;
            return this;
        }

        public CoachingTopicBuilder withExperience(double experience) {
            this.experience = experience;
            return this;
        }
    }
}
