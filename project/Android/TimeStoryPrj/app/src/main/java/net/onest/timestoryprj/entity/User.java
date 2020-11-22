package net.onest.timestoryprj.entity;
import java.util.HashSet;
import java.util.Set;

public class User {
    private Integer userId;//用户标识符
    private String userAccount;//用户账号
    private String userPassword;//用户密码
    private String userHeader;//用户头像
    private String userNickname;//用户昵称
    private long userExperience;//用户经验点
    private int userCount;//用户积分

    private UserStatus userStatus;//用户地位
    private Set<UserCard> userCards = new HashSet<>();//用户拥有的卡片
    private Set<UserRecharge> userRecharges = new HashSet<>();//用户的历史订单
    private Set<UserUnlockDynasty> userUnlockDynasties = new HashSet<>();//用户解锁的朝代集合
    private Set<UserUnlockDynastyIncident> userUnlockDynastyIncidents = new HashSet<>();//用户解锁的某个朝代的事件集合
    private Set<UserProblem> problems = new HashSet<>();//用户做过的题目


}
