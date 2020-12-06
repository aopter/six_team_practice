package net.onest.timestoryprj.activity.card;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.customview.StoryNodeView;
import net.onest.timestoryprj.entity.card.Card;

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
    @BindView(R.id.text)
    TextView tip;
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
        final Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/custom_font.ttf");
        tip.setTypeface(typeface);
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
            Toast.makeText(getApplicationContext(), "正在加载，请点击慢一些吧", Toast.LENGTH_SHORT).show();
        } else {
            clickMillis = clickTwiceMillis;
            currentStory = currentStory - 1;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(cardImg.getLayoutParams());
            if (currentStory < 0) {
                Toast.makeText(getApplicationContext(), "不能再回头啦", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "正在加载，请点击慢一些吧", Toast.LENGTH_SHORT).show();
        } else {
            clickMillis = clickTwiceMillis;
            currentStory = currentStory + 1;
            if (currentStory >= event.size()) {
                currentStory = event.size() - 1;
                Toast.makeText(getApplicationContext(), "没有更多了", Toast.LENGTH_SHORT).show();
            } else {
                storyNode.setSelectIndex(currentStory);
                story.setText(event.get(currentStory));
            }
        }
    }
}
