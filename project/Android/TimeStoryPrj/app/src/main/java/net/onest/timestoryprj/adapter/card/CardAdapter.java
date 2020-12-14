package net.onest.timestoryprj.adapter.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.customview.SpeedRecyclerView;
import net.onest.timestoryprj.entity.Dynasty;
import net.onest.timestoryprj.entity.UserUnlockDynasty;
import net.onest.timestoryprj.util.AdapterMeasureHelper;

import java.util.List;

public class CardAdapter extends SpeedRecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<UserUnlockDynasty> dynasties;
    private Context mContext;
    private OnItemClickLitener mOnItemClickLitener;

    //设置回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private AdapterMeasureHelper mCardAdapterHelper = new AdapterMeasureHelper();

    public CardAdapter(Context context, List<UserUnlockDynasty> dynasties) {
        this.dynasties = dynasties;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynasty_item, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());

        holder.tv_dynasty.setText(dynasties.get(position).getDynastyName());
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
        return dynasties.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_dynasty;

        public ViewHolder(final View view) {
            super(view);
            tv_dynasty = view.findViewById(R.id.dynasty);
        }
    }
}
