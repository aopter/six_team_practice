package net.onest.timestoryprj.activity.card;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.entity.Card;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private Animation in;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectfic_card_detail);
        ButterKnife.bind(this);
        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        Intent intent = getIntent();
        int cardId = intent.getIntExtra("cardId", -1);
        if (cardId == -1) {
            Toast.makeText(getApplicationContext(), "获取卡片详情出错啦，请重新获取", Toast.LENGTH_SHORT).show();
            cardName.setVisibility(View.INVISIBLE);
            cardStory.setVisibility(View.INVISIBLE);
            // TODO 弹窗提示获取卡片信息失败
        } else {
            cardName.setVisibility(View.VISIBLE);
            cardStory.setVisibility(View.VISIBLE);
            // TODO 从客户端获取卡片详情， 参数为 card_id
            card = new Card();
            card.setCardId(1);
            card.setCardName("杨贵妃");
//            card.setCardPicture();
            card.setCardInfo("人物简介：杨玉环（618年—907年）\n\n    出身宦门世家曾祖父杨汪是隋朝的上柱国、吏部尚du书，唐初被李世zhi民所杀，父杨玄琰，是蜀州司户，叔父杨玄珪曾任河南府土曹，杨玉环的童年是在四川度过的，10岁左右，父亲去世，她寄养在洛阳的三叔杨玄珪家。\n     杨玉环天生丽质，加上优越的教育环境，使她具备有一定的文化修养，性格婉顺，精通音律，擅歌舞，并善弹琵琶。");
            card.setCardType(1);
            card.setCardStory("开元二十五年（737年）武惠妃逝世，李瑁的母亲武惠妃是玄宗最为宠爱的妃子，在宫中的礼遇等同于皇后。玄宗因此郁郁寡欢，当时后宫数千，无可意者，有人进言杨玉环“姿质天挺，宜充掖廷”，于是唐玄宗将杨氏召入后宫之中。" + Constant.DELIMITER + "开元二十八年（740年）十月，以为玄宗母亲窦太后祈福的名义，敕书杨氏出家为女道士，道号“太真”。" +
                    Constant.DELIMITER + "天宝四年（745年），唐玄宗把韦昭训的女儿册立为寿王妃后，遂册立杨玉环为贵妃，玄宗自废掉王皇后就再未立后，因此杨贵妃就相当于皇后。" + Constant.DELIMITER +
                    "唐玄宗言国忠乱朝当诛，然贵妃无罪，本欲赦免，无奈禁军士兵皆认为贵妃乃祸国红颜，安史之乱乃因贵妃而起，不诛难慰军心、难振士气，继续包围皇帝。唐玄宗接受高力士的劝言，为求自保，不得已之下，赐死了杨贵妃。" + Constant.DELIMITER +
                    "最终杨贵妃被赐白绫一条，缢死在佛堂的梨树下，时年三十八岁，这就是白居易的《长恨歌》中的“六军不发无奈何，宛转蛾眉马前死”之典故。玄宗在安史之乱平定后回宫，曾派人去寻找杨贵妃的遗体，但未寻得。");
            cardInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
            cardInfo.setText(card.getCardInfo());
            cardInfo.startAnimation(in);
            cardName.setText(card.getCardName());
            cardPic.setImageDrawable(getResources().getDrawable(R.mipmap.role));
        }
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
