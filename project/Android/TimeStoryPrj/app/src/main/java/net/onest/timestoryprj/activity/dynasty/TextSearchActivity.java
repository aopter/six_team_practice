package net.onest.timestoryprj.activity.dynasty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.SearchIncident;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TextSearchActivity extends AppCompatActivity {

    private LinearLayout llText;
    private OkHttpClient okHttpClient;
    private String searchIncident = "/incident/search/";
    private Gson gson;
    private Typeface typeface;
    private PromptDialog promptDialog;
    @BindView(R.id.btn_pre)
    Button btnPre;
    List<SearchIncident> incidentList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    incidentList = (List<SearchIncident>) msg.obj;
                    Log.i("事件门", incidentList.toString());
                    initMsg();
                    promptDialog.dismissImmediately();
                    break;
            }
        }
    };

    private void initMsg() {
        Log.i("ii", "ii");
        for (int i = 0;i < incidentList.size();i++){
            LinearLayout ll1 = new LinearLayout(getApplicationContext());
            TextView tv1 = new TextView(getApplicationContext());
            TextView tv2 = new TextView(getApplicationContext());
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv1.setText(incidentList.get(i).getDynastyName());
            tv1.setTypeface(typeface);
            tv1.setAlpha((float) 0.7);
            tv1.setTextSize(20);
            tv1.setGravity(Gravity.CENTER);
            lp1.setMargins(100, 50, 0, 50);
            tv2.setText(incidentList.get(i).getIncidentName());
            tv2.setTypeface(typeface);
            tv2.setAlpha((float) 0.7);
            tv2.setTextSize(20);
            tv2.setGravity(Gravity.CENTER);
            tv1.setLayoutParams(lp1);
            tv2.setLayoutParams(lp1);
            ll1.addView(tv1);
            ll1.addView(tv2);
            LinearLayout ll2 = new LinearLayout(getApplicationContext());
            TextView tv3 = new TextView(getApplicationContext());
            tv3.setText(incidentList.get(i).getIncidentInfo());
            tv3.setTypeface(typeface);
            tv3.setAlpha((float) 0.7);
            tv3.setTextSize(15);
            tv3.setGravity(Gravity.CENTER);
            tv3.setLayoutParams(lp1);
            ll2.addView(tv3);
            llText.addView(ll1);
            llText.addView(ll2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_search);
        ButterKnife.bind(this);
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(1000);
        promptDialog.showLoading("正在加载");
        initOkHttp();
        initGson();
        findViews();
        setListener();
        AssetManager assets = getAssets();
        typeface = Typeface.createFromAsset(assets, "fonts/custom_fontt.ttf");
        updateTextAndDownloadMsg();
    }

    private void setListener() {

    }

    /**
     * 向服务器传文本数据并获取事件信息
     */
    private void updateTextAndDownloadMsg(){
        Intent intent = getIntent();
        String text = intent.getStringExtra("searchText");
        //  /incident/search/{userId}/keywords
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + searchIncident + Constant.User.getUserId() + "/" + text)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(()->{
            Response response = null;
            try {
                response = call.execute();
                String json = response.body().string();
                //1.得到集合类型
                Type type = new TypeToken<List<SearchIncident>>(){
                }.getType();
//                List<SearchIncident> searchIncidents = gson.fromJson(json, type);
                List<SearchIncident> searchIncidents = new ArrayList<>();
                SearchIncident s1 = new SearchIncident();
                s1.setDynastyName("唐朝");
                s1.setIncidentName("玄武门之变");
                s1.setIncidentInfo("null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=2, dynastyName='西汉', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=3, dynastyName='新朝', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=4, dynastyName='东汉', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=5, dynastyName='三国', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=6, dynastyName='西晋', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=7, dynastyName='东晋', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=8, dynastyName='十六国', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=9, dynastyName='南北朝', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=10, dynastyName='隋朝', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=11, dynastyName='唐朝', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=12, dynastyName='五代十国', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=13, dynastyName='宋朝', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreationTime=0, incidents=null, problems=null}, Dynasty{dynastyId=14, dynastyName='辽朝', dynastyTime='null', dynastyInfo='null', dynastyCreator='null', dynastyCreation");
                for (int i = 0; i < 10;i++){
                    searchIncidents.add(s1);
                }
                Log.e("incidents", searchIncidents.toString());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = searchIncidents;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void findViews() {
        llText = findViewById(R.id.ll_text);
        btnPre = findViewById(R.id.btn_pre);
    }

    /**
     * 初始化OKHTTP对象
     */
    private void initOkHttp() {
        okHttpClient = new OkHttpClient();
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

    @OnClick(R.id.btn_pre)
    void backToLastPage() {
        finish();
    }

}
