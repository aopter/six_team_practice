package net.onest.timestoryprj.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
    private DropdownListAdapter dropdownListAdapter;
    private TextView textView;
    private ImageView imageView;
    private PopupWindow popupWindow = null;
    private ArrayList<String> cardTypes = new ArrayList<>();
    private Context mContext;

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
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow == null) {
                    showPopWindow();
                } else {
                    closePopWindow();
                }
            }
        });
    }

    private void showPopWindow() {
        // 加载popupWindow的布局文件
        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infServie);
        View contentView = layoutInflater.inflate(R.layout.dropdown_list_popupwindow, null, false);
        ListView listView = contentView.findViewById(R.id.list_view);
        dropdownListAdapter = new DropdownListAdapter(getContext(), cardTypes, R.layout.dropdown_list_item);
        listView.setAdapter(dropdownListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String text = cardTypes.get(position);
                textView.setText(text);
                closePopWindow();
            }
        });
        ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.colorDropdown));
        popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(this);
    }

    /**
     * 关闭下拉列表弹窗
     */
    private void closePopWindow() {
        popupWindow.dismiss();
        popupWindow = null;
    }

    public void setDatas(ArrayList<String> types) {
        cardTypes = types;
        textView.setText(cardTypes.get(0));
    }
}