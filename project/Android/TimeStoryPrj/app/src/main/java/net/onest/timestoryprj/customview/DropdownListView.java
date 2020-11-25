package net.onest.timestoryprj.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.card.DropdownListAdapter;

import java.util.ArrayList;

public class DropdownListView extends LinearLayout {
    public DropdownListAdapter dropdownListAdapter;
    public TextView textView;
    private ImageView imageView;
    public PopupWindow popupWindow = null;
    public ArrayList<String> cardTypes = new ArrayList<>();
    public ListView listView;


    public DropdownListView(Context context) {
        super(context);
        initView();
    }

    public DropdownListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DropdownListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infServie);
        View view = layoutInflater.inflate(R.layout.dropdown_result, this, true);
        textView = view.findViewById(R.id.card_type);
        imageView = view.findViewById(R.id.down_img);
        imageView.setImageResource(R.mipmap.down);
    }

    public Context setDatas(ArrayList<String> types) {
        cardTypes = types;
        textView.setText(cardTypes.get(0));
        return getContext();
    }
}