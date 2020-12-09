package net.onest.timestoryprj.adapter.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.card.Icon;

import java.util.List;

public class ShareAdapter extends BaseAdapter {
    private List<Icon> icons;
    private Context mContext;

    @Override
    public int getCount() {
        return icons.size();
    }

    @Override
    public Object getItem(int i) {
        return icons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.share_icon_item, null);
            holder = new ViewHolder();
            holder.share = view.findViewById(R.id.share_icon);
            holder.shareName = view.findViewById(R.id.share_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.share.setImageDrawable(mContext.getResources().getDrawable(icons.get(position).getIconId()));
        holder.shareName.setText(icons.get(position).getName());
        return view;
    }

    public ShareAdapter(Context context, List<Icon> icons) {
        this.icons = icons;
        this.mContext = context;
    }

    public class ViewHolder{
        private ImageView share;
        private TextView shareName;
    }
}
