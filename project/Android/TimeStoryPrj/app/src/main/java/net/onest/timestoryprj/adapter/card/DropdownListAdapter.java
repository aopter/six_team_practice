package net.onest.timestoryprj.adapter.card;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.onest.timestoryprj.R;

import java.util.ArrayList;

public class DropdownListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> types = new ArrayList<>();
    private int itemLayoutRes;
    private ListItemView listItemView;

    public DropdownListAdapter(Context ctx, ArrayList<String> cardTypes, int itemLayoutRes) {
        mContext = ctx;
        types = cardTypes;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public Object getItem(int position) {
        return types.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // 获取list_item布局文件的视图
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(itemLayoutRes, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv = convertView.findViewById(R.id.tv);
            // 设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        // 设置数据
        listItemView.tv.setText(types.get(position));
        return convertView;
    }

    private class ListItemView {
        private TextView tv;
    }
}

