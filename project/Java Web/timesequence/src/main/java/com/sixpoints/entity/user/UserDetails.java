package com.sixpoints.entity.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "us_user_details")
public class UserDetails implements Serializable {
    private Integer userId;//用户标识符
    private String userNickname;//用户昵称
    private String userSignature;//个性签名
    private String userSex;//性别
    private String userNumber;//手机号
    private long userCreationTime;//创建时间

    @Id
    @GeneratedValue(generator = "userIdGenerator")
    @GenericGenerator(name = "userIdGenerator",strategy = "assigned")
    @Column
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(length = 20)
    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    @Column(length = 100)
    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    @Column(length = 2)
    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    @Column(length = 11)
    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    @Column
    public long getUserCreationTime() {
        return userCreationTime;
    }

    public void setUserCreationTime(long userCreationTime) {
        this.userCreationTime = userCreationTime;
    }
}
