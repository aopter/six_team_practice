package net.onest.timestoryprj.activity.dynasty;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Dynasty;
import net.onest.timestoryprj.entity.UserUnlockDynasty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {

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
    private List<TextView> tvs = new ArrayList<>();
    private String DYNASTY_LIST = "/dynasty/list";
    private String UNLOCK_DYNASTY_LIST = "/userunlockdynasty/list/1";
    private Gson gson;
    private List<Dynasty> dynasties1;
    private Handler handler;
    //定义MediaPlayer
    private MediaPlayer mediaPlayer;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        findViews();
        loadImgWithPlaceHolders();
        setListener();
        initMediaPlayer();
        //初始化gson
        initGson();
        initData();
        AssetManager assets = getAssets();
        final Typeface typeface = Typeface.createFromAsset(assets, "fonts/custom_font.ttf");
        handler = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        dynasties1 = (List<Dynasty>) msg.obj;
                        for (int i = 0;i < dynasties1.size(); i++) {
                            TextView tv = new TextView(getApplicationContext());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    300,
                                    300
                            );
                            tv.setText(dynasties1.get(i).getDynastyName());
                            tv.setTextColor(getColor(R.color.ourDynastyRed));
                            tv.setTypeface(typeface);
                            tv.setTextSize(30);
                            Log.i("cyl", dynasties1.get(i).getDynastyName());
                            if (i % 2 == 0) {
                                params.setMargins(80, 50, 0, 0);
                                tv.setLayoutParams(params);
                                llLayout1.addView(tv);
                            } else {
                                if (i == 1){
                                    params.setMargins(200, 80, 0, 0);
                                }else{
                                    params.setMargins(90, 80, 0, 0);
                                }
                                tv.setLayoutParams(params);
                                llLayout2.addView(tv);
                            }
                            int finalI = i;
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    for (int j = 0; j < Constant.UnlockDynasty.size();j++){
                                        if (Constant.UnlockDynasty.get(j).getDynastyId().equals(dynasties1.get(finalI).getDynastyId().toString())){
                                            Intent intent = new Intent();
                                            intent.setClass(getApplicationContext(), DynastyIntroduceActivity.class);
                                            intent.putExtra("dynastyId", dynasties1.get(finalI).getDynastyId().toString());
                                            Log.i("cyll", dynasties1.get(finalI).toString());
                                            startActivity(intent);
                                            break;
                                        }else{
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

    }

    /**
     * 初始化Gson对象
     */
    private void initGson() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("YY:MM:DD")
                .create();
    }

    /**
     * 初始化首页数据
     */
    private void initData() {
        getUserInfo();
        downloadUnlockDynastyList();
        downloadDynastyList();
    }

    /**
     * 从服务器获得解锁的朝代列表
     */
    //1应为Constant.User.getUserId()
    private void downloadUnlockDynastyList() {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(ServiceConfig.SERVICE_ROOT + UNLOCK_DYNASTY_LIST);
                    url.openStream();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String json1 = reader.readLine();
//                    JSONObject obj = new JSONObject(json1);
//                    String json = obj.getJSONArray("mydynasty").toString();
                    Log.i("cyl", json1);
                    //1.得到集合类型
                    Type type = new TypeToken<List<UserUnlockDynasty>>(){}.getType();
                    //2.反序列化
                    List<UserUnlockDynasty> dynasties = gson.fromJson(json1, type);
                    Constant.UnlockDynasty = dynasties;
                    Log.i("cyl1", Constant.UnlockDynasty.toString());
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
    }

    /**
     * 从服务器端获得朝代列表
     */
    private void downloadDynastyList() {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(ServiceConfig.SERVICE_ROOT  + DYNASTY_LIST);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String json1 = reader.readLine();
//                    JSONObject obj = new JSONObject(json1);
//                    String json = obj.getJSONArray("Value").toString();

                    //1.得到集合类型
                    Type type = new TypeToken<List<Dynasty>>(){}.getType();
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
    }

    private void findViews() {
        ivHeader = findViewById(R.id.iv_header);
        btnVoice = findViewById(R.id.btn_voice);
        btnSettings = findViewById(R.id.btn_settings);
        tvName = findViewById(R.id.tv_name);
        tvLevel = findViewById(R.id.tv_level);
        tvPoint = findViewById(R.id.tv_point);
        btnPlus = findViewById(R.id.btn_plus);
        btnCard = findViewById(R.id.btn_card);
        btnMyCard = findViewById(R.id.btn_my_card);
        btnMyCollections = findViewById(R.id.btn_my_collections);
        llLayout1 = findViewById(R.id.ll_layout1);
        llLayout2 = findViewById(R.id.ll_layout2);
    }

    /**
     * 将头像剪为圆形
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadImgWithPlaceHolders() {
        Glide.with(this)
                .load(getDrawable(R.mipmap.man))
                .circleCrop()
                .into(ivHeader);
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_voice:
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        btnVoice.setBackgroundResource(R.mipmap.novoice);
                    }else{
                        mediaPlayer.start();
                        btnVoice.setBackgroundResource(R.mipmap.voice);
                    }
                    break;
                case R.id.btn_plus:
                    break;
                case R.id.btn_card:
                    break;
                case R.id.btn_my_card:
                    break;
                case R.id.btn_my_collections:
                    break;
                case R.id.btn_settings:
                    break;
                case R.id.iv_header:
                    break;
            }
        }
    }

    /**
     * 初始化背景音乐
     */
    private void initMediaPlayer() {
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }
}
