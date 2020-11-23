package net.onest.timestoryprj.activity.card;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.card.CardAdapter;
import net.onest.timestoryprj.adapter.card.SpecificDynastyCardAdapter;
import net.onest.timestoryprj.entity.Card;

import java.util.ArrayList;
import java.util.List;

public class SpecificDynastyCardActivity extends AppCompatActivity {
    private RecyclerView dyanstyCardView;
    private List<Card> cards = new ArrayList<>();
    private SpecificDynastyCardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_dynasty_card);
        initDynastyCards();
        cardAdapter = new SpecificDynastyCardAdapter(getApplicationContext(), cards);
        cardAdapter.setOnItemClickLitener(new CardAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), SpectficCardDetailActivity.class);
                intent.putExtra("cardId", cards.get(position).getCardId());
                startActivity(intent);
            }
        });
        dyanstyCardView = findViewById(R.id.dynasty_cards);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        dyanstyCardView.setLayoutManager(layoutManager);
        dyanstyCardView.setAdapter(cardAdapter);
    }

    private void initDynastyCards() {
        Intent intent = getIntent();
        int dynastyId = intent.getIntExtra("dynasty", -1);
        if (dynastyId == -1) {
            Toast.makeText(getApplicationContext(), "获取卡片出错啦，请重新获取", Toast.LENGTH_SHORT).show();
        } else {
            // TODO 从客户端获取数据, 参数为 user_id 与 dynasty
        }
        for (int i = 0; i < 8; i++) {
            Card card = new Card();
            card.setCardName("card" + i);
            cards.add(card);
        }
    }
}
