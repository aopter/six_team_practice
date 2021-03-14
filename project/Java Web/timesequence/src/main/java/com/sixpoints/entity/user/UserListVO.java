package com.sixpoints.entity.user;

import java.io.Serializable;

public class UserListVO implements Serializable {
    private Integer userId;//用户标识符
    private String userHeader;//用户头像
    private String userNickname;//用户昵称
    private long userExperience;//用户经验点
    private String userSignature;//个性签名

    public UserListVO() {}

    public UserListVO(Integer userId, String userHeader, String userNickname, long userExperience, String userSignature) {
        this.userId = userId;
        this.userHeader = userHeader;
        this.userNickname = userNickname;
        this.userExperience = userExperience;
        this.userSignature = userSignature;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public long getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(long userExperience) {
        this.userExperience = userExperience;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }
}
