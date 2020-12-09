package net.onest.timestoryprj.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.user.RechargeAdapter;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Pricing;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RechargeActivity extends AppCompatActivity {
    private List<Pricing> pricings;
    private RechargeAdapter rechargeAdapter;
    @BindView(R.id.back)
    ImageView toLastPage;
    @BindView(R.id.recharges)
    GridView rechargeView;
    private Gson gson;
    private OkHttpClient client;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    Log.e("log", result);
                    Type type = new TypeToken<ArrayList<Pricing>>() {
                    }.getType();
                    pricings = gson.fromJson(result, type);
                    rechargeAdapter = new RechargeAdapter(getApplicationContext(), pricings);
                    rechargeView.setAdapter(rechargeAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        gson = new GsonBuilder()//创建GsonBuilder对象
                .serializeNulls()//允许输出Null值属性
                .create();//创建Gson对象
        getPricings();
    }

    private void getPricings() {
        new Thread() {
            @Override
            public void run() {
                Log.e("key", ServiceConfig.SERVICE_ROOT + "/pricing/list");
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/pricing/list")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // TODO 错误提示
                        Toast.makeText(getApplicationContext(), "获取充值界面失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    @OnClick(R.id.back)
    void backToLastPage() {
        finish();
    }
}
