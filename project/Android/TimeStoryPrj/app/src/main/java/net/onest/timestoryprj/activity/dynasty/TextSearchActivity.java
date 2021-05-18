package net.onest.timestoryprj.activity.dynasty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.dynasty.TextSearchAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.dialog.dynasty.EventDialogActivity;
import net.onest.timestoryprj.entity.SearchIncident;
import net.onest.timestoryprj.util.ToastUtil;

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
    private OkHttpClient okHttpClient;
    private String searchIncident = "/incident/solr/";
    private Gson gson;
    private Typeface typeface;
    private PromptDialog promptDialog;
    @BindView(R.id.btn_pre)
    Button btnPre;
    @BindView(R.id.search_incident_list)
    ListView searchIncidentResult;
    List<SearchIncident> incidentList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
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
        TextSearchAdapter textSearchAdapter = new TextSearchAdapter(this, incidentList, R.layout.item_solr_search);
        searchIncidentResult.setAdapter(textSearchAdapter);
        searchIncidentResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (incidentList.get(i).isFlag() == true) {
                    //跳转
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), EventDialogActivity.class);
                    intent.putExtra("dynastyId2", incidentList.get(i).getDynastyId().toString());
                    intent.putExtra("dynastyName", incidentList.get(i).getDynastyName());
                    intent.putExtra("incidentId", incidentList.get(i).getIncidentId().toString());
                    startActivity(intent);
                } else {
                    ToastUtil.showEncourageToast(getApplicationContext(), "您还没有解锁此朝代", 1500);
                }
            }
        });
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
    private void updateTextAndDownloadMsg() {
        Intent intent = getIntent();
        String text = intent.getStringExtra("searchText");
        //  /incident/search/{userId}/keywords
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + searchIncident + Constant.User.getUserId() + "/" + text)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            Response response = null;
            try {
                response = call.execute();
                String json = response.body().string();
                //1.得到集合类型
                Type type = new TypeToken<List<SearchIncident>>() {
                }.getType();
                List<SearchIncident> searchIncidents = gson.fromJson(json, type);
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
                .create();
    }

    @OnClick(R.id.btn_pre)
    void backToLastPage() {
        finish();
    }

}
