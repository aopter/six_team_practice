package net.onest.timestoryprj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import net.onest.timestoryprj.activity.card.DrawCardActivity;
import net.onest.timestoryprj.activity.card.MyCardActivity;
import net.onest.timestoryprj.activity.dynasty.DynastyIntroduceActivity;
import net.onest.timestoryprj.activity.dynasty.HomepageActivity;
import net.onest.timestoryprj.activity.problem.LinkLineTextActivity;
import net.onest.timestoryprj.activity.problem.SelectProblemTypeActivity;
import net.onest.timestoryprj.activity.user.UserCenterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import net.onest.timestoryprj.activity.user.LoginActivity;
import net.onest.timestoryprj.activity.user.SettingActivity;


public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnHomepage;
    private Button btnSet;

    @BindView(R.id.btn_draw_card)
    Button btnDrawCard;




    @BindView(R.id.btn_user_center)
    public Button btnHis;

    @BindView(R.id.btn_problem)
    public Button btnProblem;

    @BindView(R.id.btn_lkl_lian)
    Button btnLian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        btnHomepage = findViewById(R.id.btn_homepage);
        btnLogin = findViewById(R.id.login);
//       跳转主页
        btnSet = findViewById(R.id.btn_set);

        btnHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });

//       跳转登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });


    }

    //        跳转回家
    @OnClick(R.id.btn_user_center)
    public void userCenter() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, UserCenterActivity.class);
        Log.e("jumpHis: ", "执行");
        startActivity(intent);
    }

    //   跳转卡片
    @OnClick(R.id.btn_user_card)
    public void userCard() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MyCardActivity.class);
        Log.e("jumpHis: ", "执行");
        startActivity(intent);
    }

    //   跳转问题
    @OnClick(R.id.btn_problem)
    public void userProblem() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SelectProblemTypeActivity.class);
        Log.e("jumpHis: ", "执行");
        startActivity(intent);
    }
    @OnClick(R.id.btn_lkl_lian)
    public void jumpLianActivity(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LinkLineTextActivity.class);
        Log.e("jumpHis: ", "执行");
        startActivity(intent);
    }

    //跳转简介
    @OnClick(R.id.btn_link_details)
    public void dynastyDetails() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, DynastyIntroduceActivity.class);
        startActivity(intent);
    }

    // 跳转抽卡
    @OnClick(R.id.btn_draw_card)
    void toDrawCardPage() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, DrawCardActivity.class);
        startActivity(intent);
    }
}
