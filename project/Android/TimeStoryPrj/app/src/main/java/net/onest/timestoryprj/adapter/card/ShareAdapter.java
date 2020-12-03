package net.onest.timestoryprj.adapter.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.util.AdapterMeasureHelper;

import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    private List<Integer> icons;
    private Context mContext;
    private ShareAdapter.OnItemClickLitener mOnItemClickLitener;

    //设置回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(ShareAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private AdapterMeasureHelper mCardAdapterHelper = new AdapterMeasureHelper();

    public ShareAdapter(Context context, List<Integer> icons) {
        this.icons = icons;
        this.mContext = context;
    }

    @Override
    public ShareAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_icon_item, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ShareAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ShareAdapter.ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());

        holder.share.setImageDrawable(mContext.getResources().getDrawable(icons.get(position)));
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
        return icons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView share;

        public ViewHolder(final View view) {
            super(view);
            share = view.findViewById(R.id.share_icon);
        }
    }
}
