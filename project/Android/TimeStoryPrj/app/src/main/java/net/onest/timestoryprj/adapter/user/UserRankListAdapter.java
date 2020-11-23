package net.onest.timestoryprj.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserRankListAdapter extends BaseAdapter {

    private Context mContext;
    private List<User> users;
    private int itemLayoutRes;

    public UserRankListAdapter(Context mContext, List<User> users, int itemLayoutRes) {
        this.mContext = mContext;
        this.users = users;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if(null!=users)
            return users.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(null!=users)
            return users.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(itemLayoutRes, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.rankLevel.setText(users.get(position).getUserStatus().getStatusName());
        holder.rankHeader.setImageResource(R.mipmap.test_header);
        holder.rankName.setText("小美");
        holder.rankSign.setText("你好");
        // etc...
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.rank_header)
        ImageView rankHeader;
        @BindView(R.id.tv_user_level)
        TextView rankLevel;
        @BindView(R.id.rank_name)
        TextView rankName;
        @BindView(R.id.rank_sign)
        TextView rankSign;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
