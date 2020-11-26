package net.onest.timestoryprj.dialog.dynasty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Incident;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventDialogActivity extends AppCompatActivity {
    private TextView tvBigWord2;
    private TextView tvDialogIntro;
    private ImageView ivDialogIntroImg;
    private String UNLOCK_DYNASTY_ADD = "/userunlockdynasty/addunlockdynasty/";
    private String DYNASTY_ISPASS = "/userunlockdynasty/ispass/";
    private String INCIDENT_DETAILS_URL = "/incident/details/";
    private OkHttpClient okHttpClient;
    private Incident incident;
    private Gson gson;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dialog);
        initGson();
        initOkHttp();
        initData();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        incident = (Incident) msg.obj;

                        break;
                }
            }
        };
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        String dynastyId = intent.getStringExtra("dynastyId2");
        String incidentId = intent.getStringExtra("incidentId");
        Log.i("incident", incidentId);
        downloadIncidentDialog(incidentId);
        isPass(dynastyId);
    }

    /**
     * 下载事件详情
     */
    private void downloadIncidentDialog(String incidentId) {
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + INCIDENT_DETAILS_URL + incidentId)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(()->{
            try {
                Response response = call.execute();
                String json = response.body().string();
                Log.i("xiang", json);
                Incident incident = gson.fromJson(json, Incident.class);

                Message msg = new Message();
                msg.obj = incident;
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
        new Thread(()-> {
            try {
                Response response = call.execute();
                String json = response.body().string();
                Log.i("is", json);
                JSONObject json1 = new JSONObject(json);
                String isPass = json1.getString("result");
                if (isPass.equals("true")){
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
     * @param dynastyId
     */
    //放到对话中
    private void isPass(String dynastyId) {
        //Constant.User.getUserId()
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + DYNASTY_ISPASS + 1 + "/" + dynastyId + 1)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(()->{
            try {
                Response response  = call.execute();
                String json = response.body().string();
                Log.i("is", json);
                JSONObject json1 = new JSONObject(json);
                String isPass = json1.getString("result");
                if (isPass.equals("true")){
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
}
