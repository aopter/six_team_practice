package net.onest.timestoryprj.activity.card;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.card.CardAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.entity.UserUnlockDynasty;
import net.onest.timestoryprj.util.CardScaleHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCardActivity extends AppCompatActivity {
    @BindView(R.id.dynasty_list)
    RecyclerView dyanstiesView;
    private CardScaleHelper mCardScaleHelper;
    private CardAdapter cardAdapter;
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dyanstiesView.setLayoutManager(linearLayoutManager);
        for (UserUnlockDynasty dynasty : Constant.UnlockDynasty) {
            Log.e("dynasty", dynasty.toString());
        }
        cardAdapter = new CardAdapter(this, Constant.UnlockDynasty);
        cardAdapter.setOnItemClickLitener(new CardAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), SpecificDynastyCardActivity.class);
                intent.putExtra("dynastyId", Constant.UnlockDynasty.get(position).getDynastyId());
                startActivity(intent);
            }
        });
        dyanstiesView.setAdapter(cardAdapter);
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(dyanstiesView);
    }

    @OnClick(R.id.back)
    void backToLastPage() {
        finish();
    }
}
