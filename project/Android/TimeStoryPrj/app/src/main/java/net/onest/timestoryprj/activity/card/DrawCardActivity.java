package net.onest.timestoryprj.activity.card;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_card);
        ButterKnife.bind(this);
        final Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/custom_font.ttf");
        tip.setTypeface(typeface);
        text.setTypeface(typeface);
        getDrawCard();
        frontContainer.bringToFront();
        cardAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.back);
    }

    private void getDrawCard() {
        // TODO 从客户端获取卡片，参数user_id
        card = new Card();
        card.setCardName("yang");
        card.setCardId(1);
    }

    @OnClick(R.id.draw_card_show)
    void showCard() {
        if (!flag) {
            flag = true;
            tip.setText("恭喜你获得‘" + card.getCardName() + "’的卡片");
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
