package net.onest.timestoryprj.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.entity.HistoryDay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryTodayAdapter extends RecyclerView.Adapter<HistoryTodayAdapter.MyHolder> {

    Context context;
    List<HistoryDay> historyDays;


    public HistoryTodayAdapter(Context context) {
        this.context = context;
        this.historyDays = Constant.historyDays;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_today,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return  myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        HistoryDay historyDay = historyDays.get(position);

        holder.tvHistoryContext.setText(historyDay.getDes());
        holder.tvHistoryTime.setText(historyDay.getLunar());
        holder.tvHistoryTitle.setText(historyDay.getTitle());
    }

    @Override
    public int getItemCount() {
        return historyDays.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_history_title)
        TextView tvHistoryTitle;
        @BindView(R.id.tv_history_time)
        TextView tvHistoryTime;
        @BindView(R.id.tv_history_context)
        TextView tvHistoryContext;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
