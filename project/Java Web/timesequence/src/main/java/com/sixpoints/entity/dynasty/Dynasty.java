package com.sixpoints.entity.dynasty;

import com.sixpoints.entity.dynasty.Incident;
import com.sixpoints.entity.dynasty.Problem;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "dy_dynasty")
public class Dynasty implements Serializable {
    private Integer dynastyId;//朝代标识符
    private String dynastyName;//朝代名称
    private String dynastyTime;//朝代时间
    private String dynastyInfo;//朝代简介
    private String dynastyCreator;//创建人
    private long dynastyCreationTime;//创建时间

    private Set<Incident> incidents;//朝代中的事件
    private Set<Problem> problems;//朝代的题目

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(Integer dynastyId) {
        this.dynastyId = dynastyId;
    }

    @Column(length = 20)
    public String getDynastyName() {
        return dynastyName;
    }

    public void setDynastyName(String dynastyName) {
        this.dynastyName = dynastyName;
    }

    @Column(length = 30)
    public String getDynastyTime() {
        return dynastyTime;
    }

    public void setDynastyTime(String dynastyTime) {
        this.dynastyTime = dynastyTime;
    }

    @Column(length = 2000)
    public String getDynastyInfo() {
        return dynastyInfo;
    }

    public void setDynastyInfo(String dynastyInfo) {
        this.dynastyInfo = dynastyInfo;
    }

    @Column(length = 10)
    public String getDynastyCreator() {
        return dynastyCreator;
    }

    public void setDynastyCreator(String dynastyCreator) {
        this.dynastyCreator = dynastyCreator;
    }

    @Column
    public long getDynastyCreationTime() {
        return dynastyCreationTime;
    }

    public void setDynastyCreationTime(long dynastyCreationTime) {
        this.dynastyCreationTime = dynastyCreationTime;
    }

    @OneToMany
    @JoinColumn(name = "dynasty_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Set<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(Set<Incident> incidents) {
        this.incidents = incidents;
    }

    @OneToMany
    @JoinColumn(name = "dynasty_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Set<Problem> getProblems() {
        return problems;
    }

    public void setProblems(Set<Problem> problems) {
        this.problems = problems;
    }

    @Override
    public String toString() {
        return "Dynasty{" +
                "dynastyId=" + dynastyId +
                ", dynastyName='" + dynastyName + '\'' +
                ", dynastyTime='" + dynastyTime + '\'' +
                ", dynastyInfo='" + dynastyInfo + '\'' +
                ", dynastyCreator='" + dynastyCreator + '\'' +
                ", dynastyCreationTime=" + dynastyCreationTime +
                '}';
    }
}
