package net.onest.timestoryprj.adapter.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.card.UserCard;

import java.util.List;

public class SpecificDynastyCardAdapter extends BaseAdapter {
    private List<UserCard> cards;
    private Context mContext;
    private CardAdapter.OnItemClickLitener mOnItemClickLitener;

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int i) {
        return cards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.dynasty_card_item, null);
            holder = new ViewHolder();
            holder.cardName = view.findViewById(R.id.card_name);
            holder.cardPic = view.findViewById(R.id.card_pic);
            holder.cardNum = view.findViewById(R.id.card_num);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.cardName.setText(cards.get(position).getCardListVO().getCardName());
        Glide.with(mContext)
                .load(ServiceConfig.SERVICE_ROOT + "/picture/download/" + cards.get(position).getCardListVO().getCardPicture())
                .into(holder.cardPic);
        if (cards.get(position).getCardCount() == null) {
            holder.cardNum.setVisibility(View.GONE);
        } else {
            holder.cardNum.setText(cards.get(position).getCardCount() + "");
        }
        return view;
    }

    public SpecificDynastyCardAdapter(Context context, List<UserCard> cards) {
        this.mContext = context;
        this.cards = cards;
    }

    public class ViewHolder {
        private ImageView cardPic;
        private TextView cardName;
        private TextView cardNum;
    }

}
