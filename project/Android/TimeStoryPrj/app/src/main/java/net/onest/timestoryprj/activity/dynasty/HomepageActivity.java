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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tinytongtong.tinyutils.LogUtils;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.card.DrawCardActivity;
import net.onest.timestoryprj.activity.card.MyCardActivity;
import net.onest.timestoryprj.activity.problem.ProblemCollectionActivity;
import net.onest.timestoryprj.activity.user.RechargeActivity;
import net.onest.timestoryprj.activity.user.SettingActivity;
import net.onest.timestoryprj.activity.user.UserCenterActivity;
import net.onest.timestoryprj.adapter.user.UserRankListAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.customview.FlowerView;
import net.onest.timestoryprj.dialog.card.CustomDialog;
import net.onest.timestoryprj.entity.Dynasty;
import net.onest.timestoryprj.entity.User;
import net.onest.timestoryprj.entity.UserStatus;
import net.onest.timestoryprj.entity.UserUnlockDynasty;
import net.onest.timestoryprj.util.DensityUtil;
import net.onest.timestoryprj.util.ToastUtil;

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
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomepageActivity extends AppCompatActivity {
    int flag = 0;
    public static MediaPlayer mediaPlayer;
    private TextView tvLevel;
    private List<TextView> tvs = new ArrayList<>();
    private TextView tvPoint;
    private Button btnPlus;
    private Button btnCard;
    private Button btnMyCard;
    private Button btnMyCollections;
    private ImageView ivHeader;
    private Button btnVoice;
    private Button btnSettings;
    private OkHttpClient okHttpClient;
    private LinearLayout llLayout1;
    private LinearLayout llLayout2;
    private String DYNASTY_LIST = "/dynasty/list";
    private String UNLOCK_DYNASTY_LIST = "/userunlockdynasty/list/";
    private Gson gson;
    private List<Dynasty> dynasties1;
    private Typeface typeface;
    private ProgressBar progressBar;
    private User user;
    private long prelongTim = 0;
    private long curTime = 0;
    /** Called when the activity is first created. */

    private RelativeLayout relativeProgress;
    private HorizontalScrollView hsvDynasty;
    /**
     * Called when the activity is first created.
     */

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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            tv.setTextColor(Color.argb((float) 0.7, 0, 0, 0));
                        }
                        tv.setTypeface(typeface);
                        tv.setAlpha((float) 0.7);
                        tv.setTextSize(20);
                        if (i % 2 == 0) {
                            params.setMargins(330, 0, 0, 0);
                            tv.setLayoutParams(params);
                            llLayout1.addView(tv);
                        } else {
                            if (i == 1) {
                                params.setMargins(650, 0, 0, 0);
                            } else if (i == dynasties1.size() - 1) {
                                params.setMargins(330, 0, 330, 0);
                            } else {
                                params.setMargins(330, 0, 0, 0);
                            }
                            tv.setLayoutParams(params);
                            llLayout2.addView(tv);
                            tvs.add(tv);
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
                                if(prelongTim == 0) {
                                    prelongTim = new Date().getTime();
                                    for (int j = 0; j < Constant.UnlockDynasty.size(); j++) {
                                        if (unlockDynastyIds.contains(dynasties1.get(finalI).getDynastyId().toString())) {
                                            Intent intent = new Intent();
                                            intent.setClass(getApplicationContext(), DynastyIntroduceActivity.class);
                                            intent.putExtra("dynastyId", dynasties1.get(finalI).getDynastyId().toString());
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                                        } else {
                                            ToastUtil.showSickToast(getApplicationContext(), "该朝代未解锁", 1500);
                                        }
                                    }
                                }else{
                                    curTime = new Date().getTime();
                                    if (curTime - prelongTim < 1000){
                                    }else{
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
                                    prelongTim = curTime;
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
        initOkhttp();
        //获取等级列表
        initStatus();
        setListener();
        initSnow();
        //初始化gson
        initGson();
        initMediaPlayer();
        AssetManager assets = getAssets();
        typeface = Typeface.createFromAsset(assets, "fonts/custom_fontt.ttf");
        initData();
    }

    /**
     * 加载okhttp
     */
    private void initOkhttp() {
        okHttpClient = new OkHttpClient();
    }

    private void initStatus() {
        //加载地位
        if (Constant.userStatuses.size() < 1) {
            //请求
            Request.Builder builder1 = new Request.Builder();
            builder1.url(ServiceConfig.SERVICE_ROOT + "/status/list");
            //构造请求类
            Request request1 = builder1.build();
            final Call call1 = okHttpClient.newCall(request1);
            call1.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.d("下载失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonData = response.body().string();
                    Constant.userStatuses = gson.fromJson(jsonData, new TypeToken<List<UserStatus>>() {
                    }.getType());
                }
            });
        }

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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getUserInfo() {
        initProgress();
        loadImgWithPlaceHolders();
        tvPoint.setText(Constant.User.getUserCount() + "");
        if (Constant.User.getUserExperience() == Constant.User.getUserStatus().getStatusExperienceTop()) {
            Constant.User.setUserStatus(Constant.userStatuses.get(Constant.User.getUserStatus().getStatusId()));
        }
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
                    Constant.dynastiesName = dynasties;
                    Log.e("朝代列表", dynasties.toString());
                    Log.e("添加", Constant.dynastiesName.get(12).getDynastyName());
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
    }

    /**
     * 将头像剪为圆形
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadImgWithPlaceHolders() {
        if (Constant.User.getFlag() == 0){
            //手机号登录
            if (Constant.User.getUserHeader() == null){
                Glide.with(getApplicationContext())
                        .load(R.mipmap.man)
                        .circleCrop()
                        .into(ivHeader);
            }else {
                if (Constant.ChangeHeader == 0){
                    //未修改头像
                    Glide.with(getApplicationContext())
                            .load(ServiceConfig.SERVICE_ROOT + "/img/"+Constant.User.getUserHeader())
                            .circleCrop()
                            .signature(new ObjectKey(Constant.Random))
                            .into(ivHeader);
                }else if (Constant.ChangeHeader == 1){
                    //修改头像
                    Constant.Random = System.currentTimeMillis();
                    Log.e("下载头像",ServiceConfig.SERVICE_ROOT+"/img/"+Constant.User.getUserHeader());
                    Glide.with(getApplicationContext())
                            .load(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader())
                            .circleCrop()
                            .signature(new ObjectKey(Constant.Random))
                            .into(ivHeader);
                    Constant.ChangeHeader = 0;
                }
            }
        }else if (Constant.User.getFlag() == 1){
            //QQ登录
            Glide.with(getApplicationContext())
                    .load(Constant.User.getUserHeader())
                    .circleCrop()
                    .into(ivHeader);
        }

    }

    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (null == Constant.User || Constant.User.getUserId() == 0) {
                Toast.makeText(getApplicationContext(), "正在加载，稍后再试哦", Toast.LENGTH_SHORT).show();
                return;
            }
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
//                    抽卡界面
                    if (Constant.User.getUserCount() < Constant.descCount) {
                        Constant.showCountDialog(HomepageActivity.this);
                        return;
                    }
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
                    if (flag == 0){
                        tvLevel.setText("" + Constant.User.getUserExperience() + "/" + Constant.User.getUserStatus().getStatusExperienceTop());
                        flag = 1;
                    }else{
                        tvLevel.setText(Constant.User.getUserStatus().getStatusName());
                        flag = 0;

                    }
                    break;
//                    breakint count = relativeProgress.getChildCount();
//                    Log.e("元素个数", count + "");
//                    if (count == 1) {
//                        TextView tvExerperience = new TextView(getApplicationContext());
//                        int we = DensityUtil.dip2px(getApplicationContext(), 70);
//                        int he = DensityUtil.dip2px(getApplicationContext(), 40);
//                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(we, he);
//                        params.topMargin = 5;
//                        tvExerperience.setPadding(20, 0, 20, 0);
//                        tvExerperience.setText("" + Constant.User.getUserExperience() + "/" + Constant.User.getUserStatus().getStatusExperienceTop());
//                        tvExerperience.setTextColor(getResources().getColor(R.color.ourDynastyRed));
//                        tvExerperience.setTextSize(12);
//                        tvExerperience.setBackgroundResource(R.mipmap.button);
//                        tvExerperience.setGravity(View.TEXT_ALIGNMENT_CENTER);
//                        params.addRule(RelativeLayout.BELOW, R.id.tv_level);
//                        tvExerperience.setLayoutParams(params);
//                        tvExerperience.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                relativeProgress.removeViewAt(1);
//                            }
//                        });
//                        relativeProgress.addView(tvExerperience);
//                    };
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        flag = 0;
        setListener();
        getUserInfo();
        List<String> unlockDynastyIds = new ArrayList<>();
        for (int j = 0; j < Constant.UnlockDynasty.size(); j++) {
            unlockDynastyIds.add(Constant.UnlockDynasty.get(j).getDynastyName());
        }
        Log.e("unlock", unlockDynastyIds.toString());
        for (int i = 0; i < tvs.size();i++){
            if (unlockDynastyIds.contains(tvs.get(i).getText().toString())){
                tvs.get(i).setBackgroundResource(R.mipmap.shan);
                Log.e("tvs", tvs.get(i).getText().toString());
            }
        }
    }


}
