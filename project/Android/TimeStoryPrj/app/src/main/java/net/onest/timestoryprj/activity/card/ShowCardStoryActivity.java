package net.onest.timestoryprj.activity.card;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.entity.Card;
import net.onest.timestoryprj.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowCardStoryActivity extends AppCompatActivity {
    private Card card;
    private List<String> event = new ArrayList<>();
    int currentStory = 0;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.story1)
    TextView story1;
    @BindView(R.id.story2)
    TextView story2;
    @BindView(R.id.story3)
    TextView story3;
    @BindView(R.id.card_img)
    ImageView cardImg;
    @BindView(R.id.title_container)
    RelativeLayout titleContainer;
    private int translate;
    private Animation tran;
    private int x;
    private int y;
    private int title;
    private Animation in;
    private Animation out;
    private long clickMillis = 0;
    private boolean flag = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clickMillis = System.currentTimeMillis();
        setContentView(R.layout.activity_show_card_story);
        ButterKnife.bind(this);
        story1.setMovementMethod(ScrollingMovementMethod.getInstance());
        story2.setMovementMethod(ScrollingMovementMethod.getInstance());
        story3.setMovementMethod(ScrollingMovementMethod.getInstance());
        Intent intent = getIntent();
        card = (Card) intent.getSerializableExtra("card");
        for (String e : card.getCardStory().split(Constant.DELIMITER)) {
            event.add(e);
        }
        translate = ScreenUtil.getScreenWidth(getApplicationContext()) / 3;
        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1000);
        out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(1000);
        story1.setAnimation(out);
        story1.setText(event.get(currentStory));
        story1.setAnimation(in);
        title = titleContainer.getBottom();
        int[] location = new int[2];
        cardImg.getLocationInWindow(location);
        y = location[1]; // view距离window 顶边的距离（即y轴方向）
        tran = new TranslateAnimation(x, x + translate, y - title, y - title);
        tran.setDuration(1000);
        tran.setRepeatCount(0);
        tran.setFillAfter(true);
    }

    @OnClick(R.id.back)
    void backToLastPage() {
        finish();
    }

    @OnClick(R.id.container)
    void shiftStory() {
        if (!flag) {
            long clickTwiceMillis = System.currentTimeMillis();
            if ((clickTwiceMillis - clickMillis) < 1000) {
                Toast.makeText(getApplicationContext(), "正在加载，请点击慢一些吧", Toast.LENGTH_SHORT).show();
            } else {
                clickMillis = clickTwiceMillis;
                currentStory = currentStory + 1;
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(cardImg.getLayoutParams());
                if (currentStory >= event.size()) {
                    Toast.makeText(getApplicationContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    flag = true;
                } else {
                    if (currentStory % 3 == 0) {
                        // 第三次
                        lp.setMargins(-translate, ScreenUtil.dip2px(getApplicationContext(), 170), 0, 0);
                        cardImg.setLayoutParams(lp);
                        //lp代表某个控件，该控件可以获取到其父控件类型的LayoutParams
                        story1.startAnimation(out);
                        story1.setText(event.get(currentStory));
                        story1.startAnimation(in);
                    } else if (currentStory % 3 == 1) {
                        lp.setMargins(0, ScreenUtil.dip2px(getApplicationContext(), 170), 0, 0);
                        cardImg.setLayoutParams(lp);
                        // 第一次点击
                        cardImg.startAnimation(tran);
                        x = cardImg.getLeft();
                        story2.startAnimation(out);
                        story2.setText(event.get(currentStory));
                        story2.startAnimation(in);
                    } else if (currentStory % 3 == 2) {
                        lp.setMargins(translate, ScreenUtil.dip2px(getApplicationContext(), 170), 0, 0);
                        cardImg.setLayoutParams(lp);
                        // 第二次点击
                        cardImg.startAnimation(tran);
                        x = cardImg.getLeft();
                        story3.startAnimation(out);
                        story3.setText(event.get(currentStory));
                        story3.startAnimation(in);
                    }
                }
            }
        }
    }
}
