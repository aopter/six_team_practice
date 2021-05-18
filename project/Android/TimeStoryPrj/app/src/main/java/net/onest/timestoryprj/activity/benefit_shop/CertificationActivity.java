package net.onest.timestoryprj.activity.benefit_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.benefit_shop.CertificationAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.donate.CertificateUserBookListVO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CertificationActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.user_donate_num)
    TextView userDonateNum;
    private Gson gson;
    private OkHttpClient okHttpClient;
    String url = "/userbook/donatedlist/";
    private ImageView ivUserHead;
    private TextView tvUserName;
    private TextView tvFirstDonateTime;
    private PromptDialog promptDialog;
    private ListView lvDonateNote;
    private List<CertificateUserBookListVO> cerLists;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String json = (String) msg.obj;
                    Log.e("zhengshu", json);
                    Type type = new TypeToken<ArrayList<CertificateUserBookListVO>>() {
                    }.getType();
                    cerLists = gson.fromJson(json, type);
                    initListView();
                    promptDialog.dismissImmediately();
                    break;
            }
        }
    };

    private void initListView() {
        CertificationAdapter certificationAdapter = new CertificationAdapter(this, cerLists, R.layout.item_book);
        lvDonateNote.setAdapter(certificationAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        ButterKnife.bind(this);
        findViews();
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(1000);
        promptDialog.showLoading("正在加载");
        initGson();
        initOkHttp();
        initHead();
        downLoadCertificationMsg();
    }

    private void initHead() {
        Glide.with(getApplicationContext())
                .load(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader())
                .circleCrop()
                .signature(new ObjectKey(Constant.Random))
                .into(ivUserHead);
        tvUserName.setText(Constant.User.getUserNickname());
        if (Constant.User.getUserTotalDonateBooks() != 0) {
            tvFirstDonateTime.setText("自" + Constant.User.getUserFirstDonateTime() + "以来");
            userDonateNum.setText("您共捐赠了 " + Constant.User.getUserTotalDonateBooks() + " 本图书，由六排上分组认证，统一捐赠");
        } else {
            tvFirstDonateTime.setText("您还没有捐赠过图书，快去公益图书看看吧~");
            userDonateNum.setText("");
        }
    }

    /**
     * 下载我的证书信息
     */
    private void downLoadCertificationMsg() {
        // /userbook/donatedlist/{userId}
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + url + Constant.User.getUserId())
                .build();
        Log.e("url", ServiceConfig.SERVICE_ROOT + url + Constant.User.getUserId());
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            Response response = null;
            try {
                response = call.execute();
                String json = response.body().string();
                Message msg = new Message();
                msg.what = 1;
                msg.obj = json;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void findViews() {
        ivUserHead = findViewById(R.id.iv_user_head);
        tvUserName = findViewById(R.id.tv_user_name);
        tvFirstDonateTime = findViewById(R.id.tv_first_donate_time);
        lvDonateNote = findViewById(R.id.lv_donate_note);
    }

    /**
     * 初始化OKHTTP对象
     */
    private void initOkHttp() {
        okHttpClient = new OkHttpClient();
    }

    private void initGson() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @OnClick(R.id.back)
    void backToLastPage() {
        finish();
    }
}
