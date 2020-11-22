package net.onest.timestoryprj.entity;

import net.onest.timestoryprj.entity.Dynasty;
import net.onest.timestoryprj.entity.Incident;

public class UserUnlockDynastyIncident {
    private Integer id;//流水号

    private Dynasty dynasty;//朝代
    private Incident incident;//事件


    @Override
    public String toString() {
        return "UserUnlockDynastyIncident{" +
                "id=" + id +
                ", dynasty=" + dynasty +
                ", incident=" + incident +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dynasty getDynasty() {
        return dynasty;
    }

    public void setDynasty(Dynasty dynasty) {
        this.dynasty = dynasty;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }
}
