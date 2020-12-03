package net.onest.timestoryprj.dialog.dynasty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Incident;
import net.onest.timestoryprj.entity.UserUnlockDynasty;
import net.onest.timestoryprj.entity.UserUnlockDynastyIncident;
import net.onest.timestoryprj.util.DensityUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptButton;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventDialogActivity extends AppCompatActivity {
    private TextView tvBigWord2;
    private TextView tvDialogIntro;
    private LinearLayout llDialogLayout;
    private ImageView ivDialogIntroImg;
    private TextView tvDialog1;
    private ImageView ivDialog1Img;
    private ImageView ivDialog2Img;
    private RelativeLayout rlRelativeLayout;
    @BindView(R.id.btn_pre2)
    Button btnPre2;
    private String UNLOCK_DYNASTY_ADD = "/userunlockdynasty/addunlockdynasty/";
    private String DYNASTY_ISPASS = "/userunlockdynasty/ispass/";
    private String INCIDENT_DETAILS_URL = "/incident/details/";
    private String UNLOCK_INCIDENT_ADD = "/userincident/unlock/";
    private OkHttpClient okHttpClient;
    private Gson gson;
    private String[] txtList;
    private ObjectAnimator objectAnimator;
    private String incidentId;
    private String dynastyId;
    private String dynastyName;
    private ScrollView svScroll;
    private Incident incident;
    private Typeface typeface1;
    private Typeface typeface;
    private long prelongTim = 0;
    private long curTime = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    tvBigWord2.setText(incident.getIncidentName());
                    tvBigWord2.setTypeface(typeface);
                    tvDialogIntro.setText(incident.getIncidentInfo());
                    tvDialogIntro.setTypeface(typeface1);
                    txtList = (String[]) msg.obj;
                    Log.e("abc", String.valueOf(txtList.length));
                    ViewGroup.LayoutParams pp = rlRelativeLayout.getLayoutParams();
                    pp.height = DensityUtil.dip2px(getApplicationContext(), 110) * txtList.length;
                    rlRelativeLayout.setLayoutParams(pp);
                    svScroll.post(new Runnable(){
                        @Override
                        public void run() {
                            svScroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    tvDialog1.setText(txtList[0]);
                    tvDialog1.setTypeface(typeface1);
                    llDialogLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (prelongTim == 0){
                                prelongTim = new Date().getTime();
                                int count = rlRelativeLayout.getChildCount();
                                TextView tv = new TextView(getApplicationContext());
                                int width = DensityUtil.dip2px(getApplicationContext(), 150);
                                int height = DensityUtil.dip2px(getApplicationContext(), 100);
                                int th = DensityUtil.dip2px(getApplicationContext(), 20);
                                int tp = DensityUtil.dip2px(getApplicationContext(), 10);
                                int ml = DensityUtil.dip2px(getApplicationContext(), 30);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                                tv.setText(txtList[count]);
                                tv.setTextColor(Color.BLACK);
                                tv.setTypeface(typeface1);
                                tv.setMaxHeight(th);
                                tv.setPadding(th,tp,th,0);
                                if (count % 2 != 0) {
                                    params.setMargins(ml,0,0,0);
                                    tv.setBackgroundResource(R.mipmap.conversation2);
                                } else {
                                    params.setMargins(0,0,ml,0);
                                    tv.setBackgroundResource(R.mipmap.conversation1);
                                }
                                Log.e("count的值：", count + "");
                                for (int j = 0; j < count; j++) {
                                    View view1 = rlRelativeLayout.getChildAt(j);
                                    Log.e("view1", view1.toString());
                                    float curTranslationY = view1.getTranslationY();
                                    Log.e("curTranslationY:" + j, curTranslationY + "");
                                    objectAnimator = ObjectAnimator.ofFloat(view1, "translationY",
                                            curTranslationY, curTranslationY - 210);
                                    objectAnimator.setDuration(1000);
                                    objectAnimator.start();
                                }
//
                                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                tv.setLayoutParams(params);

                                rlRelativeLayout.addView(tv);
                                if (count == txtList.length-1) {
                                    llDialogLayout.setOnClickListener(null);
                                    long experience = Constant.User.getUserExperience();
                                    Log.i("ex1", String.valueOf(Constant.User.getUserExperience()));
                                    experience = experience + 15;
                                    Constant.User.setUserExperience(experience);
                                    Log.i("ex", String.valueOf(Constant.User.getUserExperience()));
                                    addUnlockIncidents();
                                    isPass(dynastyId);
                                }
                            }else{
                                curTime = new Date().getTime();
                                if ((curTime-prelongTim) < 1000){
                                    Log.e("iii", curTime-prelongTim + "");
                                }else{
                                    Log.e("iiie", curTime-prelongTim + "");
                                    int count = rlRelativeLayout.getChildCount();
                                    TextView tv = new TextView(getApplicationContext());
                                    int width = DensityUtil.dip2px(getApplicationContext(), 150);
                                    int height = DensityUtil.dip2px(getApplicationContext(), 100);
                                    int th = DensityUtil.dip2px(getApplicationContext(), 20);
                                    int tp = DensityUtil.dip2px(getApplicationContext(), 10);
                                    int ml = DensityUtil.dip2px(getApplicationContext(), 30);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                                    tv.setText(txtList[count]);
                                    tv.setTextColor(Color.BLACK);
                                    tv.setTypeface(typeface1);
                                    tv.setMaxHeight(th);
                                    tv.setPadding(th,tp,th,0);
                                    if (count % 2 != 0) {
                                        params.setMargins(ml,0,0,0);
                                        tv.setBackgroundResource(R.mipmap.conversation2);
                                    } else {
                                        params.setMargins(0,0,ml,0);
                                        tv.setBackgroundResource(R.mipmap.conversation1);
                                    }
                                    Log.e("count的值：", count + "");
                                    for (int j = 0; j < count; j++) {
                                        View view1 = rlRelativeLayout.getChildAt(j);
                                        Log.e("view1", view1.toString());
                                        float curTranslationY = view1.getTranslationY();
                                        Log.e("curTranslationY:" + j, curTranslationY + "");
                                        objectAnimator = ObjectAnimator.ofFloat(view1, "translationY",
                                                curTranslationY, curTranslationY - 210);
                                        objectAnimator.setDuration(1000);
                                        objectAnimator.start();
                                    }
//
                                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                    tv.setLayoutParams(params);

                                    rlRelativeLayout.addView(tv);
                                    if (count == txtList.length-1) {
                                        llDialogLayout.setOnClickListener(null);
                                        long experience = Constant.User.getUserExperience();
                                        Log.i("ex1", String.valueOf(Constant.User.getUserExperience()));
                                        experience = experience + 15;
                                        Constant.User.setUserExperience(experience);
                                        Log.i("ex", String.valueOf(Constant.User.getUserExperience()));
                                        addUnlockIncidents();
                                        isPass(dynastyId);
                                    }
                                }
                                prelongTim = curTime;
                            }
                        }
                    });
                    break;
            }
        }
    };

    /**
     * 添加看过的事件
     */
    private void addUnlockIncidents() {
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + UNLOCK_INCIDENT_ADD + Constant.User.getUserId() + "/" + dynastyId + "/" + incidentId)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(){
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String json = response.body().string();
                    Log.i("json", json);
                    JSONObject json1 = new JSONObject(json);
                    String isAdd = json1.getString("result");
                    if (isAdd.equals("true")){
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "您已看完此事件", Toast.LENGTH_LONG).show();
                        Looper.loop();
                        UserUnlockDynastyIncident unlockIncident = new UserUnlockDynastyIncident();
                        unlockIncident.setIncidentId(incident.getIncidentId());
                        unlockIncident.setIncidentName(incident.getIncidentName());
                        unlockIncident.setIncidentPicture("incident/tang-" + incidentId + ".png");
                        Constant.UnlockDynastyIncident.add(unlockIncident);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dialog);
        findViews();
        ButterKnife.bind(this);
        initGson();
        initOkHttp();
        catchException();
        AssetManager assets = getAssets();
        typeface = Typeface.createFromAsset(assets, "fonts/custom_fontt.ttf");
        typeface1 = Typeface.createFromAsset(assets, "fonts/custom_font.ttf");
        initData();
    }

    private void findViews() {
        tvBigWord2 = findViewById(R.id.tv_big_word2);
        tvDialogIntro = findViewById(R.id.tv_dialog_intro);
        tvDialog1 = findViewById(R.id.tv_dialog1);
        ivDialogIntroImg = findViewById(R.id.iv_dialog_intro_img);
        ivDialog1Img = findViewById(R.id.iv_dialog1_img);
        ivDialog2Img = findViewById(R.id.iv_dialog2_img);
        rlRelativeLayout = findViewById(R.id.rl_relativeLayout);
        btnPre2 = findViewById(R.id.btn_pre2);
        llDialogLayout = findViewById(R.id.ll_dialog_layout);
        svScroll = findViewById(R.id.sv_scroll);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        dynastyId = intent.getStringExtra("dynastyId2");
        incidentId = intent.getStringExtra("incidentId");
        dynastyName = intent.getStringExtra("dynastyName");
        Log.i("incident", incidentId);
        downloadIncidentDialog(incidentId);
        downloadIncidentImg();
    }

    /**
     * 下载图片
     */
    private void downloadIncidentImg() {
        Glide.with(this).load(ServiceConfig.SERVICE_ROOT + "/picture/download/incident/tang-1.png")
                .into(ivDialog1Img);
        Glide.with(this).load(ServiceConfig.SERVICE_ROOT + "/picture/download/incident/tang-2.png")
                .into(ivDialog2Img);
        Glide.with(this).load(ServiceConfig.SERVICE_ROOT + "/picture/download/incident/tang-other.png")
                .into(ivDialogIntroImg);
    }

    /**
     * 下载事件详情
     */
    private void downloadIncidentDialog(String incidentId) {
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + INCIDENT_DETAILS_URL + incidentId)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            try {
                Response response = call.execute();
                String json = response.body().string();
                incident = gson.fromJson(json, Incident.class);
                Log.i("xiang", incident.getIncidentDialog());
                String[] txtList = incident.getIncidentDialog().split(Constant.DELIMITER);
                Message msg = new Message();
                msg.obj = txtList;
                msg.what = 1;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 解锁下一个朝代
     */
    //放到对话中
    private void addUnlockDynasty(String dynastyId) {
        int dynastyID = Integer.parseInt(dynastyId) + 1;
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + UNLOCK_DYNASTY_ADD + Constant.User.getUserId() + "/" + dynastyID)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            try {
                Response response = call.execute();
                String json = response.body().string();
                Log.i("is", json);
                JSONObject json1 = new JSONObject(json);
                String isPass = json1.getString("result");
                if (isPass.equals("true")) {
                    Looper.prepare();
                    //解锁成功
                    Toast.makeText(getApplicationContext(), "您已解锁下一朝代", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    UserUnlockDynasty unlockDynasty = new UserUnlockDynasty();
                    unlockDynasty.setDynastyId(dynastyId);
                    unlockDynasty.setDynastyName(dynastyName);
                    unlockDynasty.setProgress(0);
                    unlockDynasty.setUserId(Constant.User.getUserId());
                    Constant.UnlockDynasty.add(unlockDynasty);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 判断下一个朝代是否可以解锁
     *
     * @param dynastyId
     */
    //放到对话中
    private void isPass(String dynastyId) {
        //Constant.User.getUserId()
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + DYNASTY_ISPASS + Constant.User.getUserId() + "/" + dynastyId)
                .build();

        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            try {
                Response response = call.execute();
                String json = response.body().string();
                Log.i("is", json);
                JSONObject json1 = new JSONObject(json);
                String isPass = json1.getString("result");
                if (isPass.equals("true")) {
                    //先判断下一朝代是否解锁，后解锁下一朝代
                    addUnlockDynasty(dynastyId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * 初始化Gson
     */
    private void initGson() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("yy:mm:dd")
                .create();
    }

    /**
     * 初始化OKHTTP对象
     */
    private void initOkHttp() {
        okHttpClient = new OkHttpClient();
    }

    private void catchException() {
        //所有线程异常拦截，由于主线程的异常都被我们catch住了，所以下面的代码拦截到的都是子线程的异常
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.d("捕获异常子线程:", Thread.currentThread().getName() +
                        "在:" + e.getStackTrace()[0].getClassName());
            }
        });
    }
    @OnClick(R.id.btn_pre2)
    void backToLastPage(){finish();}
}
