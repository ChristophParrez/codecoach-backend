package be.codecoach.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "topics")
public class Topic {

    public Topic() {}

    public Topic(String name) {
        this.name = name;
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
}
