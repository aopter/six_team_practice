package net.onest.timestoryprj.activity.card;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.card.CardAdapter;
import net.onest.timestoryprj.adapter.card.DropdownListAdapter;
import net.onest.timestoryprj.adapter.card.SpecificDynastyCardAdapter;
import net.onest.timestoryprj.customview.DropdownListView;
import net.onest.timestoryprj.entity.Card;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpecificDynastyCardActivity extends AppCompatActivity {
    @BindView(R.id.dynasty_cards)
    RecyclerView dyanstyCardView;
    private List<Card> cards = new ArrayList<>();
    private SpecificDynastyCardAdapter cardAdapter;
    private int dynastyId;
    @BindView(R.id.card_types)
    DropdownListView typeView;
    private ArrayList<String> types;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.search_card_name)
    EditText searchCardName;
    @BindView(R.id.search_delete)
    ImageView searchDelete;
    @BindView(R.id.search_btn)
    TextView searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_dynasty_card);
        ButterKnife.bind(this);
        initTypes();
        initDynastyCards();
        searchCardName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!"".equals(searchCardName.getText().toString())) {
                    searchDelete.setVisibility(View.VISIBLE);
                } else {
                    searchDelete.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initTypes() {
        if (types == null) {
            types = new ArrayList<>();
            types.add("全部卡片");
            types.add("人物卡片");
            types.add("文物卡片");
            typeView.setDatas(types);
        } else {
            typeView.setDatas(types);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchCardName.setText("");
        typeView.textView.setText(types.get(0));
    }

    private void initDynastyCards() {
        Intent intent = getIntent();
        dynastyId = intent.getIntExtra("dynasty", -1);
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
        cardAdapter = new SpecificDynastyCardAdapter(getApplicationContext(), cards);
        cardAdapter.setOnItemClickLitener(new CardAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), SpectficCardDetailActivity.class);
                intent.putExtra("cardId", cards.get(position).getCardId());
                startActivity(intent);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        dyanstyCardView.setLayoutManager(layoutManager);
        dyanstyCardView.setAdapter(cardAdapter);
    }

    @OnClick(R.id.back)
    void backToLastPage() {
        finish();
    }

    @OnClick(R.id.search_delete)
    void clearSearchContent() {
        searchCardName.setText("");
        searchDelete.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.card_types)
    void showType() {
        if (typeView.popupWindow == null) {
            showPopWindow();
        } else {
            closePopWindow();
        }
    }

    @OnClick(R.id.search_btn)
    void showSearchCards() {
        String key = searchCardName.getText().toString().trim();
        if ("".equals(key)) {
            Toast.makeText(getApplicationContext(), "请输入搜索关键字", Toast.LENGTH_SHORT).show();
        } else {
            // TODO 根据服务端数据请求，参数user_id、search_keyword、dynasty
            showCards();
        }
    }

    private void showPopWindow() {
        // 加载popupWindow的布局文件
        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) typeView.getContext().getSystemService(infServie);
        View contentView = layoutInflater.inflate(R.layout.dropdown_list_popupwindow, null, false);
        typeView.listView = contentView.findViewById(R.id.list_view);
        typeView.dropdownListAdapter = new DropdownListAdapter(typeView.getContext(), types, R.layout.dropdown_list_item);
        typeView.listView.setAdapter(typeView.dropdownListAdapter);
        typeView.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String text = typeView.cardTypes.get(position);
                if (!typeView.textView.getText().toString().equals(text)) {
                    typeView.textView.setText(text);
                    typeView.popupWindow.dismiss();
                    typeView.popupWindow = null;
                    if ("全部卡片".equals(typeView.textView.getText().toString().trim())) {
                        cards.clear();
                        // TODO 从客户端获取数据, 参数为 user_id 与 dynasty
                        showCards();
                    } else if ("人物卡片".equals(typeView.textView.getText().toString().trim())) {
                        cards.clear();
                        // TODO 从客户端获取数据, 参数为 user_id 与 dynasty
                        showCards();
                    } else if ("文物卡片".equals(typeView.textView.getText().toString().trim())) {
                        cards.clear();
                        // TODO 从客户端获取数据, 参数为 user_id 与 dynasty
                        showCards();
                    }
                }
            }
        });
        ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.colorDropdown));
        typeView.popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        typeView.popupWindow.setBackgroundDrawable(dw);
        typeView.popupWindow.setOutsideTouchable(true);
        typeView.popupWindow.showAsDropDown(typeView);
    }

    private void showCards() {
        if (cards.size() == 0) {
            Toast.makeText(getApplicationContext(), "没有相关卡片，请重新选择吧~", Toast.LENGTH_SHORT).show();
        }
        cardAdapter.notifyDataSetChanged();
    }

    /**
     * 关闭下拉列表弹窗
     */
    private void closePopWindow() {
        typeView.popupWindow.dismiss();
        typeView.popupWindow = null;
    }
}
