package com.sixpoints.entity.user;

import com.sixpoints.entity.status.UserStatus;

import java.io.Serializable;

public class UserVO implements Serializable {
    private Integer userId;//用户标识符
    private String userHeader;//用户头像
    private String userNickname;//用户昵称
    private long userExperience;//用户经验点
    private int userCount;//用户积分

    private UserStatus userStatus;//用户地位

    private String userFirstDonateTime;//用户首次捐赠图书
    private int userTotalDonateBooks;//用户共计捐赠的图书数量

    public UserVO() {
    }

    public UserVO(Integer userId, String userHeader, String userNickname, long userExperience, int userCount, UserStatus userStatus, String userFirstDonateTime, int userTotalDonateBooks) {
        this.userId = userId;
        this.userHeader = userHeader;
        this.userNickname = userNickname;
        this.userExperience = userExperience;
        this.userCount = userCount;
        this.userStatus = userStatus;
        this.userFirstDonateTime = userFirstDonateTime;
        this.userTotalDonateBooks = userTotalDonateBooks;
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

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserFirstDonateTime() {
        return userFirstDonateTime;
    }

    public void setUserFirstDonateTime(String userFirstDonateTime) {
        this.userFirstDonateTime = userFirstDonateTime;
    }

    public int getUserTotalDonateBooks() {
        return userTotalDonateBooks;
    }

    public void setUserTotalDonateBooks(int userTotalDonateBooks) {
        this.userTotalDonateBooks = userTotalDonateBooks;
    }
}
