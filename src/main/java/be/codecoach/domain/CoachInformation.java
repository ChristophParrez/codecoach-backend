package be.codecoach.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "coach_info")
public class CoachInformation {

    public CoachInformation() {
    }

    public CoachInformation(int coachXp, String introduction, String availability, List<CoachingTopic> coachingTopics) {
        this.coachXp = coachXp;
        this.introduction = introduction;
        this.availability = availability;
        this.coachingTopics = coachingTopics;
    }

    private CoachInformation(CoachInformationBuilder coachInformationBuilder) {
        this.coachXp = coachInformationBuilder.coachXp;
        this.introduction = coachInformationBuilder.introduction;
        this.availability = coachInformationBuilder.availability;
        this.coachingTopics = coachInformationBuilder.coachingTopics;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "coaching_info_id")
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

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setCoachingTopics(List<CoachingTopic> coachingTopics) {
        this.coachingTopics = coachingTopics;
    }

    public List<CoachingTopic> getCoachingTopics() {
        return coachingTopics;
    }

    public String getId() {
        return this.coachInfoId;
    }

    public void setCoachXp(int coachXp) {
        this.coachXp = coachXp;
    }

    public static class CoachInformationBuilder extends Builder<CoachInformation> {

        private String coachInfoId;
        private int coachXp;
        private String introduction;
        private String availability;
        private List<CoachingTopic> coachingTopics;

        private CoachInformationBuilder() {
        }

        public static CoachInformationBuilder coachInformation() {
            return new CoachInformationBuilder();
        }

        @Override
        public CoachInformation build() {
            return new CoachInformation(this);
        }

        public CoachInformationBuilder withCoachInfoId(String coachInfoId) {
            this.coachInfoId = coachInfoId;
            return this;
        }

        public CoachInformationBuilder withCoachXp(int coachXp) {
            this.coachXp = coachXp;
            return this;
        }

        public CoachInformationBuilder withIntroduction(String introduction) {
            this.introduction = introduction;
            return this;
        }

        public CoachInformationBuilder withAvailability(String availability) {
            this.availability = availability;
            return this;
        }

        public CoachInformationBuilder withCoachingTopics(List<CoachingTopic> coachingTopics) {
            this.coachingTopics = coachingTopics;
            return this;
        }
    }
}
