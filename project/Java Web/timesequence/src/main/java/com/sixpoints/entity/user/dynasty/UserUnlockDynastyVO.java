package com.sixpoints.entity.user.dynasty;

import java.io.Serializable;

public class UserUnlockDynastyVO implements Serializable {
    private Integer userId;//用户id
    private Integer dynastyId;//朝代id
    private String dynastyName;//朝代名称
    private Integer progress;//答对题目个数

    public UserUnlockDynastyVO() {}

    public UserUnlockDynastyVO(Integer userId, Integer dynastyId, Integer progress) {
        this.userId = userId;
        this.dynastyId = dynastyId;
        this.progress = progress;
    }

    public UserUnlockDynastyVO(Integer userId, Integer dynastyId, String dynastyName, Integer progress) {
        this.userId = userId;
        this.dynastyId = dynastyId;
        this.dynastyName = dynastyName;
        this.progress = progress;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
