package be.codecoach.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "topics")
public class Topic {

    public Topic() {}

    public Topic(String name) {
        this.name = name;
    }

    private Topic(TopicBuilder topicBuilder){
        this.topicId = topicBuilder.topicId;
        this.name = topicBuilder.name;
    }

    @Id
    @Column(name = "topic_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String topicId;

    @Column(name = "name")
    private String name;

    public String getTopicId() {
        return topicId;
    }

    public String getName() {
        return name;
    }

    public static class TopicBuilder extends Builder<Topic> {

        private String topicId;
        private String name;

        private TopicBuilder(){
        }

        public static TopicBuilder topic(){
            return new TopicBuilder();
        }

        @Override
        public Topic build() {
            return new Topic(this);
        }

        public TopicBuilder withId(String topicId){
            this.topicId = topicId;
            return this;
        }

        public TopicBuilder withName(String name){
            this.name = name;
            return this;
        }
    }
}
