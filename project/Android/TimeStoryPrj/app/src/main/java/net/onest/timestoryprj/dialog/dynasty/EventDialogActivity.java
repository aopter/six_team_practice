package net.onest.timestoryprj.dialog.dynasty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.AssetManager;
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
import net.onest.timestoryprj.util.DensityUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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
    private Button btnPre2;
    private String UNLOCK_DYNASTY_ADD = "/userunlockdynasty/addunlockdynasty/";
    private String DYNASTY_ISPASS = "/userunlockdynasty/ispass/";
    private String INCIDENT_DETAILS_URL = "/incident/details/";
    private OkHttpClient okHttpClient;
    private Gson gson;
    private String[] txtList;
    private ObjectAnimator objectAnimator;
    private String dynastyId;
    private ScrollView svScroll;
    private Incident incident;
    private Typeface typeface1;
    private Typeface typeface;
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
                    tvDialog1.setText(0 + "个carcar");
                    llDialogLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int count = rlRelativeLayout.getChildCount();
                            TextView tv = new TextView(getApplicationContext());
                            int width = DensityUtil.dip2px(getApplicationContext(), 150);
                            int height = DensityUtil.dip2px(getApplicationContext(), 100);
                            int th = DensityUtil.dip2px(getApplicationContext(), 20);
                            int tp = DensityUtil.dip2px(getApplicationContext(), 10);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                            tv.setText(count + "个carcar");
                            tv.setTypeface(typeface1);
                            tv.setMaxHeight(th);
                            tv.setPadding(th,tp,th,0);
                            if (count % 2 != 0) {
                                tv.setBackgroundResource(R.mipmap.conversation2);
                            } else {
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
                            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            tv.setLayoutParams(params);
//                                setDelay();
                            rlRelativeLayout.addView(tv);
                            if (count == 10) {
                                llDialogLayout.setOnClickListener(null);
                                isPass(dynastyId);
                                Toast.makeText(getApplicationContext(), "您已看完此事件", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dialog);
        findViews();
        initGson();
        initOkHttp();
        catchException();
        ViewGroup.LayoutParams pp = rlRelativeLayout.getLayoutParams();
        pp.height = DensityUtil.dip2px(getApplicationContext(), 110) * 10;
        rlRelativeLayout.setLayoutParams(pp);
        svScroll.post(new Runnable(){
            @Override
            public void run() {
                svScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        AssetManager assets = getAssets();
        typeface = Typeface.createFromAsset(assets, "fonts/custom_fontt.ttf");
        typeface1 = Typeface.createFromAsset(assets, "fonts/custom_font.ttf");
//        initHandler();
        initData();
    }

//    private void initHandler() {
//        HandlerThread handlerThread = new HandlerThread("MyThread");
//        handlerThread.start();
//        handler = new Handler(handlerThread.getLooper()){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//
//
//            }
//        };
//    }
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
        String incidentId = intent.getStringExtra("incidentId");
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
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + UNLOCK_DYNASTY_ADD + 1 + "/" + dynastyId + 1)
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
                    //解锁成功
                    Toast.makeText(getApplicationContext(), "您已解锁下一朝代", Toast.LENGTH_SHORT).show();
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
                .url(ServiceConfig.SERVICE_ROOT + DYNASTY_ISPASS + 1 + "/" + dynastyId + 1)
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
                .setDateFormat("YY:MM:DD")
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

}
