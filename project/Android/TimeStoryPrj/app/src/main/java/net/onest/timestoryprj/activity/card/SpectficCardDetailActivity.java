package net.onest.timestoryprj.activity.card;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.card.Card;
import net.onest.timestoryprj.util.TextUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpectficCardDetailActivity extends AppCompatActivity {
    private Card card;
    @BindView(R.id.card_img)
    ImageView cardPic;
    @BindView(R.id.card_name)
    Button cardName;
    @BindView(R.id.card_info)
    TextView cardInfo;
    @BindView(R.id.card_story)
    Button cardStory;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.text)
    TextView tip;
    private Animation in;
    private Gson gson;
    int cardId;
    private TextUtil textUtil;
    private OkHttpClient client;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    Log.e("info", result);
                    card = gson.fromJson(result, Card.class);
                    showDatas();
                    break;
            }
        }
    };

    private void showDatas() {
        textUtil = new TextUtil(cardInfo, card.getCardInfo(), 100);
        cardName.setText(card.getCardName());
        Glide.with(getApplicationContext())
                .load(ServiceConfig.SERVICE_ROOT + "/img/" + card.getCardPicture())
                .into(cardPic);
        if (card.getCardType() == 2) {
            cardStory.setVisibility(View.GONE);
        } else {
            cardStory.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectfic_card_detail);
        ButterKnife.bind(this);
        client = new OkHttpClient();
        cardInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        final Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/custom_font.ttf");
        tip.setTypeface(typeface);
        gson = new GsonBuilder()//创建GsonBuilder对象
                .serializeNulls()//允许输出Null值属性
                .create();//创建Gson对象
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        Intent intent = getIntent();
        card = (Card) intent.getSerializableExtra("card");
        if (card == null) {
            cardId = intent.getIntExtra("cardId", -1);
            if (cardId == -1) {
                Toast.makeText(getApplicationContext(), "获取卡片详情出错啦，请重新获取", Toast.LENGTH_SHORT).show();
                cardName.setVisibility(View.INVISIBLE);
                cardStory.setVisibility(View.INVISIBLE);
                // TODO 弹窗提示获取卡片信息失败
            } else {
                cardName.setVisibility(View.VISIBLE);
                cardStory.setVisibility(View.VISIBLE);
                getCardData();
            }
        } else {
            showDatas();
        }
    }

    @OnClick(R.id.card_info)
    void stopTextUtil(){
        if (textUtil.isFlag() == false){
            textUtil.setFlag(true);
        } else {
            cardInfo.setText(card.getCardInfo());
        }
    }

    private void getCardData() {
        new Thread() {
            @Override
            public void run() {
                Log.e("url", ServiceConfig.SERVICE_ROOT + "/card/details/" + cardId);
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

    @OnClick(R.id.card_story)
    void toCardStoryPage() {
        Intent intent = new Intent(getApplicationContext(), ShowCardStoryActivity.class);
        intent.putExtra("card", card);
        startActivity(intent);
    }

    @OnClick(R.id.back)
    void backToLastPage() {
        finish();
    }
}
