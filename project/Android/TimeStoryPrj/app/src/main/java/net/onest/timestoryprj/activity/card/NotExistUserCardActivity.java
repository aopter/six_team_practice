package net.onest.timestoryprj.activity.card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.card.NotExistCardAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.dialog.card.CCustomDialog;
import net.onest.timestoryprj.entity.card.Card;
import net.onest.timestoryprj.util.ToastUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotExistUserCardActivity extends AppCompatActivity {
    private int dynastyId;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.not_exist_cards)
    GridView notExistCards;
    private List<Card> cards = new ArrayList<>();
    private PromptDialog promptDialog;
    private OkHttpClient client;
    private Gson gson;
    private long clickMillis = 0;
    private long clickTwiceMillis;
    private NotExistCardAdapter notExistCardAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    Type type = new TypeToken<ArrayList<Card>>() {
                    }.getType();
                    cards = gson.fromJson(result, type);
                    initDatas();
                    break;
                case 2:
                    String result1 = (String) msg.obj;
                    Log.e("结果2", result1);
                    promptDialog.dismissImmediately();
                    if (result1.contains("false")) {
                        //捐卡失败
                        ToastUtil.showSickToast(getApplicationContext(), "购买失败", 1500);
                    } else {
                        Constant.User.setUserCount(Constant.User.getUserCount() - 100);
                        ToastUtil.showEncourageToast(getApplicationContext(), "购买成功", 1500);
                        finish();
                    }
                    break;
            }
        }
    };

    private void initDatas() {
        clickMillis = System.currentTimeMillis();
        if (cards.size() == 0) {
            ToastUtil.showEncourageToast(getApplicationContext(), "您已获得全部卡片", 1500);
        }
        notExistCardAdapter = new NotExistCardAdapter(getApplicationContext(), cards);
        notExistCards.setAdapter(notExistCardAdapter);
        promptDialog.dismissImmediately();
        notExistCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                clickTwiceMillis = System.currentTimeMillis();
                if ((clickTwiceMillis - clickMillis) > 1000) {
                    // 弹出框购买操作
                    CCustomDialog.Builder builder = new CCustomDialog.Builder(NotExistUserCardActivity.this);
                    builder.setMessage("您确定要购买此卡片吗？");
                    builder.setTitle("购买提示");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (Constant.User.getUserCount() >= 100) {
                                // 购买操作
                                promptDialog.showLoading("正在购买");
                                addUserCard(cards.get(position).getCardId());
                            } else {
                                ToastUtil.showSickToast(getApplicationContext(), "您的积分不足，无法购买", 1500);
                            }
                            dialog.dismiss();
                            //设置你的操作事项
                        }
                    });
                    builder.setNegativeButton("取消",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ToastUtil.showEncourageToast(getApplicationContext(), "您已取消购买操作", 1500);
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
                clickMillis = clickTwiceMillis;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_exist_user_card);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        dynastyId = intent.getIntExtra("dynastyId", 0);
        promptDialog = new PromptDialog(this);
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(1000);
        promptDialog.showLoading("正在加载");
        client = new OkHttpClient();
        gson = new GsonBuilder()//创建GsonBuilder对象
                .serializeNulls()//允许输出Null值属性
                .create();//创建Gson对象
        getInitDatas();
    }

    private void addUserCard(int cardId) {
        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/usercard/add/" + Constant.User.getUserId() + "/" + cardId)
                        .build();
                Log.e("url", ServiceConfig.SERVICE_ROOT + "/usercard/add/" + Constant.User.getUserId() + "/" + cardId);
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
                        message.what = 2;
                        message.obj = result;
                        Log.e("结果1", result);
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    private void getInitDatas() {
        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/usercard/not/" + Constant.User.getUserId() + "/" + dynastyId)
                        .build();
                Log.e("url", ServiceConfig.SERVICE_ROOT + "/usercard/not/" + Constant.User.getUserId() + "/" + dynastyId);
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
}
