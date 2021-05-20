package com.sixpoints.entity.user.book;

import java.io.Serializable;

/**
 * @author ASUS
 * @createTime 2021/5/5 10:03
 * @projectName demo
 * @className SpecificBookCompletedListVO.java
 * @description 已完成某公益项目的详情
 */
public class SpecificBookCompletedListVO implements Serializable {
    private String userName; // 捐赠爱心的用户编号
    private String donateTime;//捐赠时间,当process达到100时记录
    private String donateObject;//捐赠对象

    public SpecificBookCompletedListVO() {
    }

    public SpecificBookCompletedListVO(String userName, String donateTime, String donateObject) {
        this.userName = userName;
        this.donateTime = donateTime;
        this.donateObject = donateObject;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDonateTime() {
        return donateTime;
    }

    public void setDonateTime(String donateTime) {
        this.donateTime = donateTime;
    }

    public String getDonateObject() {
        return donateObject;
    }

    public void setDonateObject(String donateObject) {
        this.donateObject = donateObject;
    }

    @Override
    public String toString() {
        return "CompletedBookListVO{" +
                ", userName=" + userName +
                ", donateTime=" + donateTime +
                ", donateObject='" + donateObject + '\'' +
                '}';
    }
}
