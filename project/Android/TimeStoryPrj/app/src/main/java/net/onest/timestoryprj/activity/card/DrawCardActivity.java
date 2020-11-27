package net.onest.timestoryprj.activity.card;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.Card;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    FrameLayout cardContainer;
    @BindView(R.id.draw_card_show)
    ImageView drawCard;
    @BindView(R.id.tip)
    TextView tip;
    private boolean flag = false;
    // 获取卡片
    private Card card;
    private Animation cardAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_card);
        ButterKnife.bind(this);
        getDrawCard();
        cardContainer.setVisibility(View.GONE);
        cardAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.back);
    }

    private void getDrawCard() {
        // TODO 从客户端获取卡片，参数user_id
        card = new Card();
        card.setCardId(1);
    }

    @OnClick(R.id.draw_card_show)
    void showCard() {
        if (!flag) {
            flag = true;
            tip.setText("点击页面任意位置可直接查看卡片详情哦");
            // TODO 卡片的动画效果
            // 调用setAnimationListener方法对动画的实现过程进行监听
            cardAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onAnimationEnd(Animation animation) {//当动画结束时需要执行的行为
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.front);
                    drawCard.setBackground(getResources().getDrawable(R.mipmap.examplecard));
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
            startActivity(intent);
        }
    }

    @OnClick({R.id.card1, R.id.card2, R.id.card3, R.id.card4})
    void showcardContainerPage() {
        cardContainer.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back, R.id.to_last_view})
    void backToLastPage() {
        finish();
    }
}
