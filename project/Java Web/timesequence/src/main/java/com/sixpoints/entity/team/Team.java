package com.sixpoints.entity.team;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "te_team")
public class Team implements Serializable {
    private Integer teamId;//团队标识符
    private String teamName;//团队名称
    private String teamMember;//团队人员
    private String teamEmail;//团队邮箱

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Column(length = 100)
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Column(length = 200)
    public String getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(String teamMember) {
        this.teamMember = teamMember;
    }

    @Column(length = 100)
    public String getTeamEmail() {
        return teamEmail;
    }

    public void setTeamEmail(String teamEmail) {
        this.teamEmail = teamEmail;
    }
}
