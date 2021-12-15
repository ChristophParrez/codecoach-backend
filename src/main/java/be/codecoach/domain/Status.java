package be.codecoach.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "status")
@Entity
public class Status {

    @Id
    @Column(name = "status_name")
    private String statusName;

    public Status() {
    }

    public Status(String statusName) {
        this.statusName = statusName;
    }
}
