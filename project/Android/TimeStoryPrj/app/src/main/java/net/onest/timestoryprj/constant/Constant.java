package net.onest.timestoryprj.constant;

import net.onest.timestoryprj.entity.HistoryDay;
import net.onest.timestoryprj.entity.Rule;
import net.onest.timestoryprj.entity.User;
import net.onest.timestoryprj.entity.UserUnlockDynasty;
import net.onest.timestoryprj.entity.UserUnlockDynastyIncident;

import java.util.ArrayList;
import java.util.List;

/**
 * 安卓用到的常量 全局对象等等
 */
public class Constant {
    //分隔符
    public static String DELIMITER = "&&&";
    //当前登录的用户
    public static User User = new User();
    //历史上的今天事件集合
    public static List<HistoryDay> historyDays = new ArrayList<>();
    //当前登录用户解锁的朝代
    public static List<UserUnlockDynasty> UnlockDynasty = new ArrayList<>();
<<<<<<< Updated upstream
    //规则详情
    public static Rule rule;
    //当前登录用户解锁的某朝代的事件
    public static List<UserUnlockDynastyIncident> UnlockDynastyIncident = new ArrayList<>();
    //规则详情
    public static Rule Rule = new Rule();
=======

    //规则详情
    public static Rule rule;

    //当前登录用户解锁的某朝代的事件
    public static List<UserUnlockDynastyIncident> UnlockDynastyIncident = new ArrayList<>();

    public static List<User> UserRankList;
>>>>>>> Stashed changes
}
