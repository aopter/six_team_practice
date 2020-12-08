package net.onest.timestoryprj.activity.dynasty;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
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
import net.onest.timestoryprj.adapter.user.RechargeAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Dynasty;
import net.onest.timestoryprj.entity.User;
import net.onest.timestoryprj.entity.UserStatus;
import net.onest.timestoryprj.entity.UserUnlockDynasty;

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


public class HomepageActivity extends AppCompatActivity {
    public static MediaPlayer mediaPlayer;
    private TextView tvName;
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
    private LinearLayout linearProgress;

    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    dynasties1 = (List<Dynasty>) msg.obj;
                    for (int i = 0; i < dynasties1.size(); i++) {
//                        LinearLayout ll = new LinearLayout(getApplicationContext());
//                        ImageView iv = new ImageView(getApplicationContext());
//                        iv.setImageResource(R.mipmap.redflag);
                        TextView tv = new TextView(getApplicationContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                300,
                                300
                        );
                        tv.setGravity(Gravity.CENTER);
                        tv.setText(dynasties1.get(i).getDynastyName());
                        tv.setTextColor(getColor(R.color.ourDynastyRed));
                        tv.setTypeface(typeface);
                        tv.setTextSize(25);
                        if (i % 2 == 0) {
                            params.setMargins(80, 30, 0, 0);
                            tv.setLayoutParams(params);
                            llLayout1.addView(tv);
                        } else {
                            if (i == 1) {
                                params.setMargins(200, 30, 0, 0);
                            } else {
                                params.setMargins(90, 30, 0, 0);
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
                                tv.setBackgroundResource(R.mipmap.fff);
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
        //初始化gson
        initGson();
        initMediaPlayer();
        AssetManager assets = getAssets();
        typeface = Typeface.createFromAsset(assets, "fonts/custom_fontt.ttf");
        initData();
        initProgress();
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
//        getUserInfo();
        downloadUnlockDynastyList();
        initProgress();
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
//                    JSONObject obj = new JSONObject(json1);
//                    String json = obj.getJSONArray("mydynasty").toString();
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
        tvPoint.setText((int) Constant.User.getUserExperience());
        tvLevel.setText(Constant.User.getUserStatus().getStatusName());
//        tvName.setText(Constant.User.getUserNickname());
//        user = Constant.User;
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
//        tvName = findViewById(R.id.tv_name);
        tvLevel = findViewById(R.id.tv_level);
        tvPoint = findViewById(R.id.tv_point);
        btnPlus = findViewById(R.id.btn_plus);
        btnCard = findViewById(R.id.btn_card);
        btnMyCard = findViewById(R.id.btn_my_card);
        btnMyCollections = findViewById(R.id.btn_my_collections);
        llLayout1 = findViewById(R.id.ll_layout1);
        llLayout2 = findViewById(R.id.ll_layout2);
        progressBar = findViewById(R.id.experience_progress);
        linearProgress = findViewById(R.id.linear_progress);
    }

    /**
     * 将头像剪为圆形
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadImgWithPlaceHolders() {
//        ServiceConfig.SERVICE_ROOT + Constant.User.getUserHeader();
        Glide.with(this)
                .load(getDrawable(R.mipmap.man))
                .circleCrop()
                .into(ivHeader);
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
                    int count = linearProgress.getChildCount();
                    Log.e("元素个数",count+"");
                    if (count == 1){
                        TextView tvExerperience = new TextView(getApplicationContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(210,420);
                        params.topMargin = 10;
                        tvExerperience.setPadding(20,0,20,0);
                        tvExerperience.setText("玩家经验:"+'\n'+Constant.User.getUserExperience()+"/"+Constant.User.getUserStatus().getStatusExperienceTop());
                        tvExerperience.setTextColor(getResources().getColor(R.color.whrite));
                        tvExerperience.setTextSize(12);
                        tvExerperience.setBackgroundResource(R.color.ourDynastyRed);
                        tvExerperience.setLayoutParams(params);
                        linearProgress.addView(tvExerperience);
                    }else if (count == 2){
                        linearProgress.removeViewAt(1);
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
