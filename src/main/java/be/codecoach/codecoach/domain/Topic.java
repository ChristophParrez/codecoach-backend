package be.codecoach.codecoach.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @Column(name = "topic_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String topic_id;

    @Column(name = "name")
    private String name;
}
