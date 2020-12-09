package net.onest.timestoryprj.activity.card;

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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.dynasty.HomepageActivity;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.dialog.card.CustomDialog;
import net.onest.timestoryprj.entity.card.Card;
import net.onest.timestoryprj.entity.card.Icon;
import net.onest.timestoryprj.util.ScreenUtil;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//抽卡

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
    @BindView(R.id.share)
    ImageView btnShare;
    @BindView(R.id.icon_view)
    GridView iconView;
    @BindView(R.id.share_container)
    RelativeLayout shareContainer;
    @BindView(R.id.e_r_code)
    ImageView ERCode;
    @BindView(R.id.join)
    TextView join;
    private List<Icon> icons;
    // 抽卡所消耗积分
    private int descCount = 60;
    // 是否已点击
    private boolean flag = false;

    private boolean isFlag = false;
    // 获取卡片
    private Card card;
    private Animation cardAnimation;
    private AnimatorSet animatorSet;
    private OkHttpClient client;
    private Gson gson;
    // 是否在分享状态
    private boolean isShareing = false;

    /**
     * 文本类型
     */
    public static int TEXT = 0;
    /**
     * 图片类型
     */
    public static int DRAWABLE = 1;
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
        if (Constant.User.getUserCount() >= descCount) {
            back.setVisibility(View.VISIBLE);
            width = ScreenUtil.dip2px(getApplicationContext(), 120);
            height = ScreenUtil.dip2px(getApplicationContext(), 180);
            final Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/custom_font.ttf");
            tip.setTypeface(typeface);
            text.setTypeface(typeface);
            client = new OkHttpClient();
            frontContainer.bringToFront();
            cardAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.back);
            gson = new GsonBuilder()//创建GsonBuilder对象
                    .serializeNulls()//允许输出Null值属性
                    .create();//创建Gson对象
            getDrawCard();
        } else {
            showDialog();
        }
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
        if (!isShareing) {
            if (!flag) {
                // TODO 恭喜
                flag = true;
                shareContainer.setVisibility(View.VISIBLE);
                toLastView.setVisibility(View.VISIBLE);
                btnShare.setVisibility(View.VISIBLE);
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
                                .load(ServiceConfig.SERVICE_ROOT + "/img/" + card.getCardPicture())
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
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, drawCard, "ivGetCard").toBundle());
                }
                //开始下一个activity     android:transitionName="ivGetCard"
            }
        }
    }

    @OnClick({R.id.card1, R.id.card2, R.id.card3, R.id.card4})
    void showCardContainerPage() {
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

    @OnClick(R.id.share)
    void toShare() {
        shareContainer.setVisibility(View.VISIBLE);
        toLastView.setVisibility(View.INVISIBLE);
        join.setVisibility(View.VISIBLE);
        ERCode.setVisibility(View.VISIBLE);
        shareContainer.bringToFront();
        ScreenShot sh = new ScreenShot();
        sh.start();
        try {
            sh.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isShareing = true;
        Intent intent = new Intent(DrawCardActivity.this, ShareCardActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.card_container, R.id.draw_card_show})
    void showOriginPage() {
        if (isFlag) {
            shareContainer.setVisibility(View.GONE);
            toLastView.setVisibility(View.VISIBLE);
            join.setVisibility(View.INVISIBLE);
            ERCode.setVisibility(View.INVISIBLE);
            shareContainer.bringToFront();
            isShareing = false;
        }
    }

    private class ScreenShot extends Thread {
        @Override
        public void run() {
            // 获取屏幕
            View view = getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Constant.shareBitmap = view.getDrawingCache();
        }
    }

    private void showDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("您的积分不足，快去选择朝代进行答题来赚取积分吧~");
        builder.setButtonConfirm("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrawCardActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });
        CustomDialog customDialog = builder.create();
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
    }
}
