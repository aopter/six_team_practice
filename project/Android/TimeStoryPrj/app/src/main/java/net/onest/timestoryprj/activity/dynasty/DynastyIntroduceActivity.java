package net.onest.timestoryprj.activity.dynasty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Dynasty;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DynastyIntroduceActivity extends AppCompatActivity {

    private TextView tvDynastyName;
    private TextView tvDynastyIntro;
    private Button btnQuestions;
    private Button btnDetails;
    private String dynastyName;
    private String dynastyId;
    private String DYNASTY_INFO;
    private Gson gson;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynasty_introduce);
        findViews();
        initGson();
        initData();
        setListener();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        Dynasty dynasty1 = (Dynasty) msg.obj;
                        tvDynastyName.setText(dynasty1.getDynastyName());
                        tvDynastyIntro.setText(dynasty1.getDynastyInfo());
                        break;
                }
            }
        };
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        btnDetails.setOnClickListener(myListener);
        btnQuestions.setOnClickListener(myListener);
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

    private void initData() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("dynastyId");
        downloadDynastyIntro(id);
        Log.i("cyl", id);
    }

    /**
     * 从服务器获取朝代简介
     */
    private void downloadDynastyIntro(final String id) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(ServiceConfig.SERVICE_ROOT + DYNASTY_INFO + "?id=" + id);
                    url.openStream();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String json = reader.readLine();
                    Dynasty dynasty = gson.fromJson(json, Dynasty.class);

                    Message msg = new Message();
                    msg.obj = dynasty;
                    msg.what = 1;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void findViews() {
        tvDynastyIntro = findViewById(R.id.tv_dynasty_intro);
        tvDynastyName = findViewById(R.id.tv_dynasty_name);
        btnQuestions = findViewById(R.id.btn_questions);
        btnDetails = findViewById(R.id.btn_details);
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_questions:
                    break;
                case R.id.btn_details:
                    break;
            }
        }
    }
}
