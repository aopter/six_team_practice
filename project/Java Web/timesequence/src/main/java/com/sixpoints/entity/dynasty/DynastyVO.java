package com.sixpoints.entity.dynasty;

import java.io.Serializable;

public class DynastyVO implements Serializable {
    private Integer dynastyId;//朝代标识符
    private String dynastyName;//朝代名称
    private String dynastyTime;//朝代时间
    private String dynastyInfo;//朝代简介

    public DynastyVO() {}

    public DynastyVO(Integer dynastyId, String dynastyName, String dynastyTime, String dynastyInfo) {
        this.dynastyId = dynastyId;
        this.dynastyName = dynastyName;
        this.dynastyTime = dynastyTime;
        this.dynastyInfo = dynastyInfo;
    }

    public Integer getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(Integer dynastyId) {
        this.dynastyId = dynastyId;
    }

    public String getDynastyName() {
        return dynastyName;
    }

    public void setDynastyName(String dynastyName) {
        this.dynastyName = dynastyName;
    }

    public String getDynastyTime() {
        return dynastyTime;
    }

    public void setDynastyTime(String dynastyTime) {
        this.dynastyTime = dynastyTime;
    }

    public String getDynastyInfo() {
        return dynastyInfo;
    }

    public void setDynastyInfo(String dynastyInfo) {
        this.dynastyInfo = dynastyInfo;
    }
}
