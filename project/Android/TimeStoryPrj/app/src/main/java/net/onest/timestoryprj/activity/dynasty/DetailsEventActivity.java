package net.onest.timestoryprj.activity.dynasty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.dynasty.EventListAdapter;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Dynasty;
import net.onest.timestoryprj.entity.Incident;
import net.onest.timestoryprj.util.DensityUtil;
import net.onest.timestoryprj.util.HorizontalListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsEventActivity extends AppCompatActivity {
    private GridView hlvTimeline;
    private List<Incident> incidentList = new ArrayList<>();
    private EventListAdapter eventListAdapter;
    private TextView tvBigWord;
    private Gson gson;
    private Handler handler;
    private OkHttpClient okHttpClient;
    private String INCIDENT_URL = "/incident/list/";
    private String UNLOCK_INCIDENT_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event);
        findViews();
        initGson();
        initOkHttp();
        initData();

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        incidentList = (List<Incident>) msg.obj;
                        initAdapter();
                }
            }
        };
    }

    /**
     * 初始化Adapter
     */
    private void initAdapter() {
        eventListAdapter = new EventListAdapter(this, incidentList, R.layout.item_incident);
        ViewGroup.LayoutParams params = hlvTimeline.getLayoutParams();
        params.width = DensityUtil.dip2px(getApplicationContext(), 110) * incidentList.size();
        hlvTimeline.setLayoutParams(params);
        hlvTimeline.setNumColumns(incidentList.size());
        hlvTimeline.setAdapter(eventListAdapter);
        Log.i("msg", "测试");
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        String dynastyId = intent.getStringExtra("dynastyId1");
        String dynastyName = intent.getStringExtra("dynastyName1");
        downloadUnlockIncidentList();
        downloadIncidentList(dynastyId);
        String word = "历史上的" + dynastyName;
        AssetManager assets = getAssets();
        final Typeface typeface = Typeface.createFromAsset(assets, "fonts/custom_font.ttf");
        tvBigWord.setTypeface(typeface);
        tvBigWord.setText(word);
    }

    /**
     * 下载已解锁的事件列表
     */
    private void downloadUnlockIncidentList() {

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
            try {
                Response response = call.execute();
                String json1 = response.body().string();
                //1.得到集合类型
                Type type = new TypeToken<List<Incident>>(){}.getType();
                //2.反序列化
                List<Incident> incident = gson.fromJson(json1, type);
                Log.i("cyl", incident.toString());
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
                .setDateFormat("YY:MM:DD")
                .create();
    }

    /**
     * 初始化OKHTTP对象
     */
    private void initOkHttp() {
        okHttpClient = new OkHttpClient();
    }

    private void findViews() {
        hlvTimeline = findViewById(R.id.hlv_timeline);
        tvBigWord = findViewById(R.id.tv_big_word);
    }
}
