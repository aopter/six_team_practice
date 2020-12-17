package net.onest.timestoryprj.activity.dynasty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.dynasty.EventListAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.dialog.card.CustomDialog;
import net.onest.timestoryprj.entity.Incident;
import net.onest.timestoryprj.entity.UserUnlockDynastyIncident;
import net.onest.timestoryprj.util.DensityUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsEventActivity extends AppCompatActivity {
    private ImageView ivDengLong;
    private GridView hlvTimeline;
    private List<Incident> incidentList = new ArrayList<>();
    private EventListAdapter eventListAdapter;
    private TextView tvBigWord;
    private Gson gson;
    private OkHttpClient okHttpClient;
    private String dynastyId;
    private String dynastyName;
    private String INCIDENT_URL = "/incident/list/";
    private String UNLOCK_INCIDENT_URL = "/userincident/list/";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    incidentList = (List<Incident>) msg.obj;
                    if (incidentList.size() != 0){
                        Log.e("事件", String.valueOf(incidentList.size()));
                        initAdapter();
                    }
                    else{
                        showIncidentDialog();
                    }
                    break;
            }
        }
    };

    /**
     * 没有事件时，显示弹窗
     */
    private void showIncidentDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("此朝代事件还未解锁，看看其他朝代吧！");
        builder.setButtonConfirm("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        CustomDialog customDialog = builder.create();
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event);
        findViews();
        initGson();
        initOkHttp();
        initData();


    }

    /**
     * 初始化Adapter
     */
    private void initAdapter() {
        eventListAdapter = new EventListAdapter(this, incidentList, R.layout.item_incident, dynastyId, dynastyName, this);
        ViewGroup.LayoutParams params = hlvTimeline.getLayoutParams();
        params.width = DensityUtil.dip2px(getApplicationContext(), 110) * incidentList.size();
        hlvTimeline.setLayoutParams(params);
        hlvTimeline.setNumColumns(incidentList.size());
        hlvTimeline.setAdapter(eventListAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        dynastyId = intent.getStringExtra("dynastyId1");
        dynastyName = intent.getStringExtra("dynastyName1");
        downloadUnlockIncidentList(dynastyId);
        downloadIncidentList(dynastyId);
        String word = "历史上的" + dynastyName;
        AssetManager assets = getAssets();
        final Typeface typeface = Typeface.createFromAsset(assets, "fonts/custom_fontt.ttf");
        tvBigWord.setTypeface(typeface);
        tvBigWord.setText(word);
        initGlide();
    }

    private void initGlide() {
        Glide.with(this)
                .asGif()
                .load(R.mipmap.housedenglong)
                .into(ivDengLong);
    }

    /**
     * 下载已解锁的事件列表
     */
    private void downloadUnlockIncidentList(String dynastyId) {
        //Constant.User.getUserId()
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + UNLOCK_INCIDENT_URL + Constant.User.getUserId() + "/" + dynastyId)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            Response response = null;
            try {
                response = call.execute();
                String json = response.body().string();
                Log.i("cccccc", json);
                //1.得到集合类型
                Type type = new TypeToken<List<UserUnlockDynastyIncident>>() {
                }.getType();
                //2.反序列化
                List<UserUnlockDynastyIncident> unlockDynastyIncidentList = gson.fromJson(json, type);
                Constant.UnlockDynastyIncident = unlockDynastyIncidentList;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 下载事件列表
     */
    private void downloadIncidentList(String dynastyId) {
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + INCIDENT_URL + dynastyId)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            Response response = null;
            try {
                response = call.execute();
                String json1 = response.body().string();
                //1.得到集合类型
                Type type = new TypeToken<List<Incident>>() {
                }.getType();
                //2.反序列化
                List<Incident> incident = gson.fromJson(json1, type);
                Log.i("cyll", incident.toString());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = incident;
                handler.sendMessage(msg);
            } catch (IOException e) {
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
        okHttpClient = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
    }

    private void findViews() {
        hlvTimeline = findViewById(R.id.hlv_timeline);
        tvBigWord = findViewById(R.id.tv_big_word);
        ivDengLong = findViewById(R.id.iv_denglong);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "加载");
        initData();
    }


}
