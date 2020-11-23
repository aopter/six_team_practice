package net.onest.timestoryprj.activity.card;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.Card;

public class SpectficCardDetailActivity extends AppCompatActivity {
    private Card card;
    private ImageView cardPic;
    private Button cardName;
    private TextView cardInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectfic_card_detail);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        int cardId = intent.getIntExtra("cardId", -1);
        if (cardId == -1) {
            Toast.makeText(getApplicationContext(), "获取卡片详情出错啦，请重新获取", Toast.LENGTH_SHORT).show();
        } else {
            // TODO 从客户端获取卡片详情， 参数为 card_id
            card = new Card();
            card.setCardId(1);
            card.setCardName("杨贵妃");
            card.setCardInfo("人物简介：（618年—907年）\n是继隋朝之后的大一统中原王朝，共历二十一帝，享国二百八十九年。\n 隋末天下群雄并起，617年唐国公李渊于晋阳起兵，次年称帝建立唐朝，定都长          安。唐太宗继位后开创贞观之治，为盛唐奠定基础。唐高宗承贞观遗风开创“永 徽之治”，并于657年建东都洛阳 [3-7]  。690年，武则天改国号为周， [8]  705年       神龙革命后，恢复唐国号。 [9-10]  唐玄宗即位后缔造全盛的开元盛世， 天宝末全国人口达八千万左右。 [12-15]  安史之乱后藩镇割据、宦官专权导致国力渐衰；中后期经唐宪宗元和中兴、唐武宗会昌中兴、唐宣宗大中之治国势复振。878年爆发黄巢起义破坏了唐朝统治根基，907年朱温篡唐，唐朝覆亡。");
            card.setCardType(1);
            cardInfo = findViewById(R.id.card_info);
            cardInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
            cardInfo.setText(card.getCardInfo());
        }
    }
}
