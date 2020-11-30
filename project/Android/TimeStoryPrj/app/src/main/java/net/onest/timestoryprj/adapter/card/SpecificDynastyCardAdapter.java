package net.onest.timestoryprj.adapter.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Card;
import net.onest.timestoryprj.entity.UserCard;

import java.util.List;

public class SpecificDynastyCardAdapter extends RecyclerView.Adapter<SpecificDynastyCardAdapter.ViewHolder> {
    private List<UserCard> cards;
    private Context mContext;
    private View view;
    private CardAdapter.OnItemClickLitener mOnItemClickLitener;

    //设置回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(CardAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public SpecificDynastyCardAdapter(Context context, List<UserCard> cards) {
        this.mContext = context;
        this.cards = cards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynasty_card_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.cardName.setText(cards.get(position).getCardListVO().getCardName());
        Glide.with(mContext)
                .load(ServiceConfig.SERVICE_ROOT + "/picture/download/" + cards.get(position).getCardListVO().getCardPicture())
                .into(holder.cardPic);
        if (cards.get(position).getCardCount() == 1 || cards.get(position).getCardCount() == null) {
            holder.cardNum.setVisibility(View.GONE);
        } else {
            holder.cardNum.setText(cards.get(position).getCardCount() + "");
        }
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardPic;
        private TextView cardName;
        private TextView cardNum;

        public ViewHolder(final View view) {
            super(view);
            cardName = view.findViewById(R.id.card_name);
            cardPic = view.findViewById(R.id.card_pic);
            cardNum = view.findViewById(R.id.card_num);
        }
    }
}
