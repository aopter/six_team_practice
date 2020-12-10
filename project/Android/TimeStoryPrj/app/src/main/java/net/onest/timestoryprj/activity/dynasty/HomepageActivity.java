package net.onest.timestoryprj.activity.dynasty;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.card.DrawCardActivity;
import net.onest.timestoryprj.activity.card.MyCardActivity;
import net.onest.timestoryprj.activity.problem.ProblemCollectionActivity;
import net.onest.timestoryprj.activity.user.RechargeActivity;
import net.onest.timestoryprj.activity.user.SettingActivity;
import net.onest.timestoryprj.activity.user.UserCenterActivity;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.customview.FlowerView;
import net.onest.timestoryprj.entity.Dynasty;
import net.onest.timestoryprj.entity.User;
import net.onest.timestoryprj.entity.UserStatus;
import net.onest.timestoryprj.entity.UserUnlockDynasty;
import net.onest.timestoryprj.util.DensityUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
public class HomepageActivity extends AppCompatActivity {
    public static MediaPlayer mediaPlayer;
    private TextView tvLevel;
    private TextView tvPoint;
    private Button btnPlus;
    private Button btnCard;
    private Button btnMyCard;
    private Button btnMyCollections;
    private ImageView ivHeader;
    private Button btnVoice;
    private Button btnSettings;
    private LinearLayout llLayout1;
    private LinearLayout llLayout2;
    private String DYNASTY_LIST = "/dynasty/list";
    private String UNLOCK_DYNASTY_LIST = "/userunlockdynasty/list/";
    private Gson gson;
    private List<Dynasty> dynasties1;
    private Typeface typeface;
    private ProgressBar progressBar;
    private User user;
    private RelativeLayout relativeProgress;
    private HorizontalScrollView hsvDynasty;
    /** Called when the activity is first created. */
    private FlowerView mFlowerView;
    // 屏幕宽度
    public static int screenWidth;
    // 屏幕高度
    public static int screenHeight;
    Timer myTimer = null;
    TimerTask mTask = null;
    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    dynasties1 = (List<Dynasty>) msg.obj;
                    for (int i = 0; i < dynasties1.size(); i++) {
                        TextView tv = new TextView(getApplicationContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                400,
                                400
                        );
                        tv.setBackgroundResource(R.mipmap.shanhui);
                        tv.setGravity(Gravity.CENTER);
                        tv.setText(dynasties1.get(i).getDynastyName());
                        tv.setTextColor(Color.argb((float) 0.7, 0, 0, 0));
                        tv.setTypeface(typeface);
                        tv.setAlpha((float) 0.7);
                        tv.setTextSize(20);
                        if (i % 2 == 0) {
                            params.setMargins(330, 0, 0, 0);
                            tv.setLayoutParams(params);
                            llLayout1.addView(tv);
                        } else {
                            if (i == 1) {
                                params.setMargins(580, 0, 0, 0);
                            } else {
                                params.setMargins(330, 0, 0, 0);
                            }
                            tv.setLayoutParams(params);
//                            ll.addView(iv);
//                            ll.addView(tv);
                            llLayout2.addView(tv);
                        }
                        int finalI = i;
                        List<String> unlockDynastyIds = new ArrayList<>();
                        for (int j = 0; j < Constant.UnlockDynasty.size(); j++) {
                            unlockDynastyIds.add(Constant.UnlockDynasty.get(j).getDynastyId());
                            if (unlockDynastyIds.contains(dynasties1.get(i).getDynastyId().toString())) {
                                tv.setBackgroundResource(R.mipmap.shan);
                            }
                        }
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                for (int j = 0; j < Constant.UnlockDynasty.size(); j++) {
                                    if (unlockDynastyIds.contains(dynasties1.get(finalI).getDynastyId().toString())) {
                                        Intent intent = new Intent();
                                        intent.setClass(getApplicationContext(), DynastyIntroduceActivity.class);
                                        intent.putExtra("dynastyId", dynasties1.get(finalI).getDynastyId().toString());
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                                        break;
                                    } else {
                                        Toast.makeText(getApplicationContext(), "该朝代未解锁", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                    break;
                case 2:
                    mFlowerView.inva();
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        findViews();
        loadImgWithPlaceHolders();
        setListener();
        initSnow();
        //初始化gson
        initGson();
        initMediaPlayer();
        AssetManager assets = getAssets();
        typeface = Typeface.createFromAsset(assets, "fonts/custom_fontt.ttf");
        initData();
        initProgress();
        Log.e("user", Constant.User.toString());
    }

    private void initSnow() {
        mFlowerView = findViewById(R.id.flowerview);
        screenWidth = getWindow().getWindowManager().getDefaultDisplay()
                .getWidth();
        screenHeight = getWindow().getWindowManager().getDefaultDisplay()
                .getHeight();
        DisplayMetrics dis = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dis);
        float de = dis.density;
        mFlowerView.setWH(screenWidth, screenHeight, de);
        mFlowerView.loadFlower();
        mFlowerView.addRect();
        myTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        };
        myTimer.schedule(mTask, 3000, 10);
    }

    /**
     * 初始化Gson对象
     */
    private void initGson() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("yy:mm:dd")
                .create();
    }
    /**
     * 初始化首页数据
     */
    private void initData() {
        getUserInfo();
        downloadUnlockDynastyList();
    }
    /**
     * 初始化进度条
     */
    private void initProgress() {
        long userExperience = Constant.User.getUserExperience();
        UserStatus userStatus = Constant.User.getUserStatus();
        long experMax = userStatus.getStatusExperienceTop();
        long experMin = userStatus.getStatusExperienceLow();
        long experOnStatus = experMax - experMin;
        DecimalFormat df = new DecimalFormat("0.00");
        String rate = df.format((float) (userExperience - experMin) / experOnStatus);
        double exRate = Double.parseDouble(rate);
        int progress = (int) (exRate * 100);
        progressBar.setProgress(progress);
    }

    /**
     * 从服务器获得解锁的朝代列表
     */
    //1应为Constant.User.getUserId()
    private void downloadUnlockDynastyList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ServiceConfig.SERVICE_ROOT + UNLOCK_DYNASTY_LIST + Constant.User.getUserId());
                    url.openStream();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String json1 = reader.readLine();
                    //1.得到集合类型
                    Type type = new TypeToken<List<UserUnlockDynasty>>() {
                    }.getType();
                    //2.反序列化
                    List<UserUnlockDynasty> dynasties = gson.fromJson(json1, type);
                    Constant.UnlockDynasty = dynasties;
                    Log.i("cyl1", Constant.UnlockDynasty.toString());
                    downloadDynastyList();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * 通过跳转获得用户信息
     */
    private void getUserInfo() {
        tvPoint.setText(Constant.User.getUserCount() + "");
        tvLevel.setText(Constant.User.getUserStatus().getStatusName());
    }
    /**
     * 从服务器端获得朝代列表
     */
    private void downloadDynastyList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ServiceConfig.SERVICE_ROOT + DYNASTY_LIST);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String json1 = reader.readLine();
                    //1.得到集合类型
                    Type type = new TypeToken<List<Dynasty>>() {
                    }.getType();
                    //2.反序列化
                    List<Dynasty> dynasties = gson.fromJson(json1, type);

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = dynasties;
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
    private void setListener() {
        MyListener myListener = new MyListener();
        btnVoice.setOnClickListener(myListener);
        btnSettings.setOnClickListener(myListener);
        btnMyCollections.setOnClickListener(myListener);
        btnMyCard.setOnClickListener(myListener);
        btnCard.setOnClickListener(myListener);
        btnPlus.setOnClickListener(myListener);
        ivHeader.setOnClickListener(myListener);
        tvLevel.setOnClickListener(myListener);
    }

    private void findViews() {
        ivHeader = findViewById(R.id.iv_header);
        btnVoice = findViewById(R.id.btn_voice);
        btnSettings = findViewById(R.id.btn_settings);
//        tvName = findViewById(R.id.tv);
        tvLevel = findViewById(R.id.tv_level);
        tvPoint = findViewById(R.id.tv_point);
        btnPlus = findViewById(R.id.btn_plus);
        btnCard = findViewById(R.id.btn_card);
        btnMyCard = findViewById(R.id.btn_my_card);
        btnMyCollections = findViewById(R.id.btn_my_collections);
        llLayout1 = findViewById(R.id.ll_layout1);
        llLayout2 = findViewById(R.id.ll_layout2);
        progressBar = findViewById(R.id.experience_progress);
        relativeProgress = findViewById(R.id.relative_progress);
        hsvDynasty = findViewById(R.id.hsv_dynasty);
    }

    /**
     * 将头像剪为圆形
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadImgWithPlaceHolders() {
        Log.e("ss", String.valueOf(getDrawable(R.mipmap.man)));
        if (Constant.User.getUserHeader() == null) {
            Glide.with(this)
                    .load(getDrawable(R.mipmap.man))
                    .circleCrop()
                    .into(ivHeader);
        }else{
            Glide.with(this)
                    .load(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader())
                    .circleCrop()
                    .into(ivHeader);
        }
    }

    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_voice:
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        btnVoice.setBackgroundResource(R.mipmap.novoice);
                    } else {
                        mediaPlayer.start();
                        btnVoice.setBackgroundResource(R.mipmap.voice);
                    }
                    break;
                case R.id.btn_plus:
                    Intent intent5 = new Intent(HomepageActivity.this, RechargeActivity.class);
                    startActivity(intent5);
                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    break;
                case R.id.btn_card:
                    Intent intent3 = new Intent(HomepageActivity.this, DrawCardActivity.class);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    break;
                case R.id.btn_my_card:
                    Intent intent4 = new Intent(HomepageActivity.this, MyCardActivity.class);
                    startActivity(intent4);
                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    break;
                case R.id.btn_my_collections:
                    Intent intent2 = new Intent(HomepageActivity.this, ProblemCollectionActivity.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    break;
                case R.id.btn_settings:
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    break;
                case R.id.iv_header:
//                    跳转
                    Intent intent1 = new Intent(HomepageActivity.this, UserCenterActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    break;
                case R.id.tv_level:
                    int count = relativeProgress.getChildCount();
                    Log.e("元素个数", count + "");
                    if (count == 1) {
                        TextView tvExerperience = new TextView(getApplicationContext());
                        int we = DensityUtil.dip2px(getApplicationContext(), 70);
                        int he = DensityUtil.dip2px(getApplicationContext(), 40);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(we, he);
                        params.topMargin = 5;
                        tvExerperience.setPadding(20,0,20,0);
                        tvExerperience.setText(""+Constant.User.getUserExperience()+"/"+Constant.User.getUserStatus().getStatusExperienceTop());
                        tvExerperience.setTextColor(getResources().getColor(R.color.ourDynastyRed));
                        tvExerperience.setTextSize(12);
                        tvExerperience.setBackgroundResource(R.mipmap.button);
                        params.addRule(RelativeLayout.BELOW, R.id.tv_level);
                        tvExerperience.setLayoutParams(params);
                        relativeProgress.addView(tvExerperience);
                    } else if (count == 2) {
                        relativeProgress.removeViewAt(1);
                    }
                    break;
            }
        }
    }

    /**
     * 初始化背景音乐
     */
    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music1);
        }
        if (mediaPlayer.isPlaying()) {
            btnVoice.setBackgroundResource(R.mipmap.voice);
        } else {
            btnVoice.setBackgroundResource(R.mipmap.novoice);
        }
        mediaPlayer.setLooping(true);
    }

}
