package net.onest.timestoryprj.activity.benefit_shop;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.customview.CusTextView;
import net.onest.timestoryprj.entity.card.Card;
import net.onest.timestoryprj.util.TextUtil;
import net.onest.timestoryprj.util.ToastUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DonateCardDetailActivity extends AppCompatActivity {
    private int cardNum;
    private int cardId;
    private int process;
    private int processId;
    private int maxCount; // 记录捐满还需几张
    private Card card;
    @BindView(R.id.card_img)
    ImageView cardPic;
    @BindView(R.id.card_name)
    Button cardName;
    @BindView(R.id.card_info)
    TextView cardInfo;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.btn_donate_card)
    Button btnDonateCard;
    @BindView(R.id.process_now)
    TextView processNow;
    @BindView(R.id.reduce_num)
    ImageView reudceNum;
    @BindView(R.id.increase_num)
    ImageView increaseNum;
    @BindView(R.id.donate_num)
    TextView donateNum;
    private int currentDonateNum = 1;
    private Gson gson;
    private boolean flag = false; // 捐赠之后的项目是否能完成
    private OkHttpClient client;
    private PromptDialog promptDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    card = gson.fromJson(result, Card.class);
                    promptDialog.dismissImmediately();
                    showDatas();
                    break;
                case 2:
                    String result1 = (String) msg.obj;
                    if (result1.contains("false")) {
                        //捐卡失败
                        ToastUtil.showSickToast(getApplicationContext(), "捐卡成功", 1500);
                    } else {
                        if (process + currentDonateNum * 10 >= 100) {
                            ToastUtil.showEncourageToast(getApplicationContext(), "捐赠卡片成功，卡片捐出后进度100%的公益项目会在我的证书中展示哦~", 1500);
                        } else {
                            ToastUtil.showEncourageToast(getApplicationContext(), "卡片捐赠成功，进度加" + currentDonateNum * 10 + "%", 1500);
                        }
                        //捐卡成功
                        Intent intent = new Intent(getApplicationContext(), DonateShopActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_card_detail);
        ButterKnife.bind(this);
        promptDialog = new PromptDialog(this);
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(1000);
        promptDialog.showLoading("正在加载");
        client = new OkHttpClient();
        cardInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        gson = new GsonBuilder()//创建GsonBuilder对象
                .serializeNulls()//允许输出Null值属性
                .create();//创建Gson对象
        getOriginalDatas();
    }

    @OnClick(R.id.btn_donate_card)
    void donateCard() {
        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/userbook/donate/" + Constant.User.getUserId() + "/" + cardId + "/" + currentDonateNum + "/" + processId)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // TODO获取卡片内容失败
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Message message = new Message();
                        message.what = 2;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    @OnClick(R.id.reduce_num)
    void reduceDonateNum() {
        if (currentDonateNum == 1) {
            ToastUtil.showCryToast(getApplicationContext(), "卡片数量不能再减少了", 1500);
            currentDonateNum = 1;
            return;
        }
        currentDonateNum--;
        donateNum.setText(currentDonateNum + "");
    }

    @OnClick(R.id.increase_num)
    void increaseDonateNum() {
        // 两个上限值：捐赠100%，与自己拥有的卡片数量
        if (currentDonateNum >= cardNum || currentDonateNum >= maxCount) {
            ToastUtil.showCryToast(getApplicationContext(), "已经到捐赠的最大数量", 1500);
            return;
        }
        currentDonateNum++;
        donateNum.setText(currentDonateNum + "");
    }

    // T展示卡片名称、简介、图片、数量、项目进度
    private void showDatas() {
        cardInfo.setText(card.getCardInfo());
        cardName.setText(card.getCardName());
        Glide.with(getApplicationContext())
                .load(ServiceConfig.SERVICE_ROOT + "/img/" + card.getCardPicture())
                .into(cardPic);
        processNow.setText("您现在的捐赠进度为：" + process + "/100");
        donateNum.setText("1");
    }

    private void getOriginalDatas() {
        Intent intent = getIntent();
        processId = Integer.parseInt(intent.getStringExtra("processId"));
        process = Integer.parseInt(intent.getStringExtra("process"));
        cardId = Integer.parseInt(intent.getStringExtra("cardId"));
        cardNum = Integer.parseInt(intent.getStringExtra("cardNum"));
        maxCount = (100 - process) / 10;
        getCardData();
    }

    private void getCardData() {
        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/card/details/" + cardId)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // TODO获取卡片内容失败
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
