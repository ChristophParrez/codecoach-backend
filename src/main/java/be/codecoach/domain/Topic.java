package be.codecoach.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "topics")
public class Topic {

    public Topic() {}

    public Topic(String name) {
        this.name = name;
    }

    private Topic(TopicBuilder topicBuilder){
        this.name = topicBuilder.name;
    }

    @Id
    @Column(name = "topic_id")
    private String name;


    public String getName() {
        return name;
    }

    public static class TopicBuilder extends Builder<Topic> {

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

        public TopicBuilder withName(String name){
            this.name = name;
            return this;
        }
    }
}
