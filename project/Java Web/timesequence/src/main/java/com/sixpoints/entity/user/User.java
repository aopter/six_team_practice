package com.sixpoints.entity.user;

import com.sixpoints.entity.status.UserStatus;
import com.sixpoints.entity.user.book.UserBookProcess;
import com.sixpoints.entity.user.card.UserCard;
import com.sixpoints.entity.user.dynasty.UserProblem;
import com.sixpoints.entity.user.dynasty.UserUnlockDynasty;
import com.sixpoints.entity.user.dynasty.UserUnlockDynastyIncident;
import com.sixpoints.entity.user.recharge.UserRecharge;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "us_user")
public class User {
    private Integer userId;//用户标识符
    private String userAccount;//用户账号
    private String userPassword;//用户密码
    private String userHeader;//用户头像
    private String userNickname;//用户昵称
    private long userExperience;//用户经验点
    private int userCount;//用户积分

    private UserStatus userStatus;//用户地位

    private long userFirstDonateTime;//用户首次捐赠图书
    private int userTotalDonateBooks;//用户共计捐赠的图书数量

    private Set<UserCard> userCards = new HashSet<>();//用户拥有的卡片
    private Set<UserRecharge> userRecharges = new HashSet<>();//用户的历史订单
    private Set<UserUnlockDynasty> userUnlockDynasties = new HashSet<>();//用户解锁的朝代集合
    private Set<UserUnlockDynastyIncident> userUnlockDynastyIncidents = new HashSet<>();//用户解锁的某个朝代的事件集合
    private Set<UserProblem> problems = new HashSet<>();//用户做过的题目
    private Set<UserBookProcess> userBookProcesses = new HashSet<>();//用户捐赠的

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(length = 20)
    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    @Column(length = 100)
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Column(length = 100)
    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    @Column(length = 20)
    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    @Column
    public long getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(long userExperience) {
        this.userExperience = userExperience;
    }

    @Column
    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    @Column
    public long getUserFirstDonateTime() {
        return userFirstDonateTime;
    }

    public void setUserFirstDonateTime(long userFirstDonateTime) {
        this.userFirstDonateTime = userFirstDonateTime;
    }

    @Column(columnDefinition = "0")
    public int getUserTotalDonateBooks() {
        return userTotalDonateBooks;
    }

    public void setUserTotalDonateBooks(int userTotalDonateBooks) {
        this.userTotalDonateBooks = userTotalDonateBooks;
    }

    @ManyToOne
    @JoinColumn(name = "status_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @OneToMany
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Set<UserCard> getUserCards() {
        return userCards;
    }

    public void setUserCards(Set<UserCard> userCards) {
        this.userCards = userCards;
    }

    @OneToMany(mappedBy = "user")
    public Set<UserRecharge> getUserRecharges() {
        return userRecharges;
    }

    public void setUserRecharges(Set<UserRecharge> userRecharges) {
        this.userRecharges = userRecharges;
    }

    @OneToMany
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Set<UserUnlockDynasty> getUserUnlockDynasties() {
        return userUnlockDynasties;
    }

    public void setUserUnlockDynasties(Set<UserUnlockDynasty> userUnlockDynasties) {
        this.userUnlockDynasties = userUnlockDynasties;
    }

    @OneToMany
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Set<UserUnlockDynastyIncident> getUserUnlockDynastyIncidents() {
        return userUnlockDynastyIncidents;
    }

    public void setUserUnlockDynastyIncidents(Set<UserUnlockDynastyIncident> userUnlockDynastyIncidents) {
        this.userUnlockDynastyIncidents = userUnlockDynastyIncidents;
    }

    @OneToMany
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Set<UserProblem> getProblems() {
        return problems;
    }

    public void setProblems(Set<UserProblem> problems) {
        this.problems = problems;
    }

    @OneToMany(mappedBy = "user")
    public Set<UserBookProcess> getUserBookProcesses() {
        return userBookProcesses;
    }

    public void setUserBookProcesses(Set<UserBookProcess> userBookProcesses) {
        this.userBookProcesses = userBookProcesses;
    }
}
