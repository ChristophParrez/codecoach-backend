package be.codecoach.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "locations")
@Entity
public class Location {

    @Id
    @Column(name = "name")
    private String name;

    public Location(String name) {
        this.name = name;
    }

    public Location() {
    }
}
