package com.sixpoints.entity.user.dynasty;

import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.Incident;

import javax.persistence.*;

@Entity
@Table(name = "us_user_unlock_dynasty_incident")
public class UserUnlockDynastyIncident {
    private Integer id;//流水号

    private Dynasty dynasty;//朝代
    private Incident incident;//事件

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "dynasty_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Dynasty getDynasty() {
        return dynasty;
    }

    public void setDynasty(Dynasty dynasty) {
        this.dynasty = dynasty;
    }

    @ManyToOne
    @JoinColumn(name = "incident_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }
}
