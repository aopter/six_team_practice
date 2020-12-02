package net.onest.timestoryprj.activity.card;

import androidx.annotation.NonNull;
//抽卡
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Card;
import net.onest.timestoryprj.entity.User;
import net.onest.timestoryprj.util.ScreenUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DrawCardActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.card1)
    ImageView card1;
    @BindView(R.id.card2)
    ImageView card2;
    @BindView(R.id.card3)
    ImageView card3;
    @BindView(R.id.card4)
    ImageView card4;
    @BindView(R.id.to_last_view)
    Button toLastView;
    @BindView(R.id.card_container)
    LinearLayout cardContainer;
    @BindView(R.id.front_container)
    LinearLayout frontContainer;
    @BindView(R.id.draw_card_show)
    ImageView drawCard;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.text)
    TextView text;
    private boolean flag = false;
    private boolean isFlag = false;
    // 获取卡片
    private Card card;
    private Animation cardAnimation;
    private AnimatorSet animatorSet;
    private OkHttpClient client;
    private Gson gson;
    int width;
    int height;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    Log.e("info", result);
                    card = gson.fromJson(result, Card.class);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_card);
        ButterKnife.bind(this);
        // TODO 记得删除
        Constant.User = new User();
        Constant.User.setUserId(1);
        width = ScreenUtil.dip2px(getApplicationContext(), 120);
        height = ScreenUtil.dip2px(getApplicationContext(), 180);
        final Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/custom_font.ttf");
        tip.setTypeface(typeface);
        text.setTypeface(typeface);
        client = new OkHttpClient();
        getDrawCard();
        frontContainer.bringToFront();
        cardAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.back);
        gson = new GsonBuilder()//创建GsonBuilder对象
                .serializeNulls()//允许输出Null值属性
                .create();//创建Gson对象
    }

    private void getDrawCard() {
        new Thread() {
            @Override
            public void run() {
                Log.e("draw", ServiceConfig.SERVICE_ROOT + "/card/draw/" + Constant.User.getUserId());
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/card/draw/" + Constant.User.getUserId())
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
                        Log.e("log", result);
                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    @OnClick(R.id.draw_card_show)
    void showCard() {
        if (!flag) {
            // TODO 恭喜
            flag = true;
            tip.setText("恭喜你获得‘" + card.getCardName() + "’的卡片");
            // 调用setAnimationListener方法对动画的实现过程进行监听
            cardAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onAnimationEnd(Animation animation) {//当动画结束时需要执行的行为
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.front);
                    drawCard.setBackground(getResources().getDrawable(R.mipmap.card_bg));
                    Glide.with(getApplicationContext())
                            .load(ServiceConfig.SERVICE_ROOT + "/picture/download/" + card.getCardPicture())
                            .into(drawCard);
                    drawCard.startAnimation(animation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            drawCard.startAnimation(cardAnimation);
        } else {
            Intent intent = new Intent(getApplicationContext(), SpectficCardDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("cardId", card.getCardId());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//有版本限制
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,drawCard,"ivGetCard").toBundle());
            }
            //开始下一个activity     android:transitionName="ivGetCard"
            intent.putExtra("card", card);
            startActivity(intent);
        }
    }

    @OnClick({R.id.card1, R.id.card2, R.id.card3, R.id.card4})
    void showcardContainerPage() {
        if (!isFlag) {
            animatorSet = new AnimatorSet();
            ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(
                    cardContainer,
                    "alpha",
                    0, 0.8f
            );
            alphaAnim.setDuration(400);
            ObjectAnimator alphaAnim1 = ObjectAnimator.ofFloat(
                    frontContainer,
                    "alpha",
                    0.95f, 0
            );
            alphaAnim1.setDuration(100);
            animatorSet.setDuration(1000);
            animatorSet.play(alphaAnim1).before(alphaAnim);
            animatorSet.start();
            isFlag = true;
            cardContainer.bringToFront();
        }
        Log.e("card", isFlag + "");
    }


    @OnClick({R.id.back, R.id.to_last_view})
    void backToLastPage() {
        finish();
    }
}
