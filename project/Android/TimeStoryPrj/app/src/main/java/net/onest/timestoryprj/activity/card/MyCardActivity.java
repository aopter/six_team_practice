package net.onest.timestoryprj.activity.card;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.card.CardAdapter;
import net.onest.timestoryprj.entity.Dynasty;
import net.onest.timestoryprj.util.CardScaleHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCardActivity extends AppCompatActivity {
    @BindView(R.id.dynasty_list)
    RecyclerView dyanstiesView;
    private CardScaleHelper mCardScaleHelper;
    private CardAdapter cardAdapter;
    private List<Dynasty> dynasties = new ArrayList<>();
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.text)
    TextView tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        ButterKnife.bind(this);
        final Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/custom_font.ttf");
        tip.setTypeface(typeface);
        initRecyclerView();
    }

    private void initRecyclerView() {
        initDyansty();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dyanstiesView.setLayoutManager(linearLayoutManager);
        cardAdapter = new CardAdapter(this, dynasties);
        cardAdapter.setOnItemClickLitener(new CardAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), SpecificDynastyCardActivity.class);
                // TODO 现为指定朝代
                intent.putExtra("dynasty", 11);
                startActivity(intent);
            }
        });
        dyanstiesView.setAdapter(cardAdapter);
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(dyanstiesView);
    }

    /**
     * 获得已解锁的朝代,向服务端申请数据
     */
    private void initDyansty() {
        // TODO 朝代数据从客户端获取，参数为user_id
        Dynasty dynasty = new Dynasty();
        dynasty.setDynastyName("唐朝");
        dynasties.add(dynasty);
        Dynasty dynasty1 = new Dynasty();
        dynasty1.setDynastyName("宋朝");
        dynasties.add(dynasty1);
        Dynasty dynasty2 = new Dynasty();
        dynasty2.setDynastyName("元朝");
        dynasties.add(dynasty2);
        Dynasty dynasty3 = new Dynasty();
        dynasty3.setDynastyName("明朝");
        dynasties.add(dynasty3);
        Dynasty dynasty4 = new Dynasty();
        dynasty4.setDynastyName("清朝");
        dynasties.add(dynasty4);

    }

    @OnClick(R.id.back)
    void backToLastPage() {
        finish();
    }
}
