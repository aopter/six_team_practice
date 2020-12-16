package net.onest.timestoryprj.constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import net.onest.timestoryprj.dialog.card.CustomDialog;
import net.onest.timestoryprj.entity.HistoryDay;
import net.onest.timestoryprj.entity.Problem;
import net.onest.timestoryprj.entity.Rule;
import net.onest.timestoryprj.entity.User;
import net.onest.timestoryprj.entity.UserDetails;
import net.onest.timestoryprj.entity.UserStatus;
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
    //规则详情
    public static Rule rule;
    //当前登录用户解锁的某朝代的事件
    public static List<UserUnlockDynastyIncident> UnlockDynastyIncident = new ArrayList<>();
    //规则详情
    public static Rule Rule = new Rule();

    public static List<User> UserRankList;

    public static List<Problem> userProblems;//题目列表

    public static UserDetails UserDetails = new UserDetails();

    public static Bitmap shareBitmap;

    public static List<UserStatus> userStatuses = new ArrayList<>();
    //用户是否更改图片，未更改为0，更改后为1
    public static int ChangeHeader;
    public static long Random;
    public static int  descCount = 60;

<<<<<<< Updated upstream
//    积分不足提示
    public static void showCountDialog(Context context) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setTitle("积分不足");
        builder.setMessage("您的积分不足，快去选择朝代进行答题来赚取积分吧~");

        CustomDialog customDialog = builder.create();
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
        builder.setButtonConfirm("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
    }
=======
    public static int ChangeHeader;

    public static long Random;

>>>>>>> Stashed changes
}
