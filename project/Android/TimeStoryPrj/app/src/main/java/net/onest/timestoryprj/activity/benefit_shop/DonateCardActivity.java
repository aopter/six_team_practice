package net.onest.timestoryprj.activity.benefit_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.card.SpecificDynastyCardAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.card.UserCard;
import net.onest.timestoryprj.util.ToastUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DonateCardActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.all_cards)
    GridView cardView;
    private List<UserCard> userCards = new ArrayList<>();
    private SpecificDynastyCardAdapter cardAdapter;
    private OkHttpClient client;
    private Gson gson;
    private long clickMillis = 0;
    private long clickTwiceMillis;
    private PromptDialog promptDialog;
    private int processId;
    private int process;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    userCards.clear();
                    String result = (String) msg.obj;
                    Type type = new TypeToken<ArrayList<UserCard>>() {
                    }.getType();
                    userCards = gson.fromJson(result, type);
                    initDatas();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_card);
        ButterKnife.bind(this);
        promptDialog = new PromptDialog(this);
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(1000);
        promptDialog.showLoading("正在加载");
        client = new OkHttpClient();
        gson = new GsonBuilder()//创建GsonBuilder对象
                .serializeNulls()//允许输出Null值属性
                .create();//创建Gson对象
        // 获取的上个Intent信息
        initOriginalData();
    }

    private void initOriginalData() {
        Intent intent = getIntent();
        processId = intent.getIntExtra("processId", -1);
        process = intent.getIntExtra("process", -1);
        getAllCards();
    }

    private void initDatas() {
        clickMillis = System.currentTimeMillis();
        if (userCards.size() == 0) {
            ToastUtil.showSickToast(getApplicationContext(), "您还没有获得卡片，请获取卡片后再来吧", 1500);
        }
        cardAdapter = new SpecificDynastyCardAdapter(getApplicationContext(), userCards);
        cardView.setAdapter(cardAdapter);
        cardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                clickTwiceMillis = System.currentTimeMillis();
                if ((clickTwiceMillis - clickMillis) > 1000) {
                    Intent intent = new Intent(getApplicationContext(), DonateCardDetailActivity.class);
                    intent.putExtra("cardId", userCards.get(position).getCardListVO().getCardId());
                    intent.putExtra("cardNum", userCards.get(position).getCardCount());
                    intent.putExtra("process", process);
                    intent.putExtra("processId", processId);
                    startActivity(intent);
                }
                clickMillis = clickTwiceMillis;
            }
        });
        promptDialog.dismissImmediately();
    }

    @OnClick(R.id.back)
    void backToLastPage() {
        finish();
    }

    private void getAllCards() {
        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/usercard/all/" + Constant.User.getUserId())
                        .build();
                Log.e("datas", ServiceConfig.SERVICE_ROOT + "/usercard/all/" + Constant.User.getUserId());
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 获取失败
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

    @Override
    protected void onResume() {
        super.onResume();
        // 重新获取
        getAllCards();
    }
}
