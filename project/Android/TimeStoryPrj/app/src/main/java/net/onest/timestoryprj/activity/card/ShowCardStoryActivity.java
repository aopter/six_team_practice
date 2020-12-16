package net.onest.timestoryprj.activity.card;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.customview.CusTextView;
import net.onest.timestoryprj.customview.StoryNodeView;
import net.onest.timestoryprj.entity.card.Card;
import net.onest.timestoryprj.util.ToastUtil;

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
    @BindView(R.id.former_story)
    LinearLayout formerStory;
    @BindView(R.id.story)
    TextView story;
    @BindView(R.id.next_story)
    LinearLayout nextStory;
    @BindView(R.id.card_img)
    ImageView cardImg;
    @BindView(R.id.role_story)
    CusTextView tip;
    @BindView(R.id.story_process)
    StoryNodeView storyNode;
    @BindView(R.id.title_container)
    LinearLayout titleContainer;
    private Animation in;
    private Animation out;
    private long clickMillis = 0;
    private long clickTwiceMillis;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card_story);
        clickMillis = System.currentTimeMillis();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        card = (Card) intent.getSerializableExtra("card");
        for (String e : card.getCardStory().split(Constant.DELIMITER)) {
            event.add(e);
        }
        storyNode.setNodeList(event);
        storyNode.setSelectIndex(0);
        storyNode.setOnClickListener(new StoryNodeView.OnClickListener() {
            @Override
            public void onCircleClick(int postion) {
                currentStory = postion;
                story.setText(event.get(currentStory));
                storyNode.setSelectIndex(postion);
            }
        });
        Glide.with(getApplicationContext())
                .load(ServiceConfig.SERVICE_ROOT + "/img/" + card.getCardPicture())
                .into(cardImg);
        defineViewAndAnimation();
    }

    private void defineViewAndAnimation() {
        tip.setText("'" + card.getCardName() + "'的那些事");
        story.setMovementMethod(ScrollingMovementMethod.getInstance());
        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1000);
        out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(1000);
        story.setAnimation(out);
        story.setText(event.get(currentStory));
        story.setAnimation(in);
    }

    @OnClick(R.id.back)
    void backToLastPage() {
        finish();
    }

    @OnClick(R.id.former_story)
    void formerStory() {
        clickTwiceMillis = System.currentTimeMillis();
        if ((clickTwiceMillis - clickMillis) < 250) {
            if (currentStory > 0) {
                ToastUtil.showCryToast(getApplicationContext(), "跟不上你的脚步了，点击慢一点吧", 1500);
            }
        } else {
            clickMillis = clickTwiceMillis;
            currentStory = currentStory - 1;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(cardImg.getLayoutParams());
            if (currentStory < 0) {
                ToastUtil.showSickToast(getApplicationContext(), "不能再回头啦", 1500);
                currentStory = 0;
            } else {
                storyNode.setSelectIndex(currentStory);
                story.setText(event.get(currentStory));
            }
        }
    }

    @OnClick(R.id.next_story)
    void nextStory() {
        clickTwiceMillis = System.currentTimeMillis();
        if ((clickTwiceMillis - clickMillis) < 250) {
            if (currentStory < event.size() - 1) {
                ToastUtil.showCryToast(getApplicationContext(), "跟不上你的脚步了，点击慢一点吧", 1500);
            }
        } else {
            clickMillis = clickTwiceMillis;
            currentStory = currentStory + 1;
            if (currentStory >= event.size()) {
                currentStory = event.size() - 1;
                ToastUtil.showSickToast(getApplicationContext(), "没有更多了", 1500);
            } else {
                storyNode.setSelectIndex(currentStory);
                story.setText(event.get(currentStory));
            }
        }
    }
}
