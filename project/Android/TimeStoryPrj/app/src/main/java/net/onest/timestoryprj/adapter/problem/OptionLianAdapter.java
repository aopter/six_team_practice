package net.onest.timestoryprj.adapter.problem;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.problem.ProblemInfoActivity;
import net.onest.timestoryprj.entity.problem.OrderBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionLianAdapter extends RecyclerView.Adapter<OptionLianAdapter.MyViewHolder> {

    List<OrderBean> options;

    public OptionLianAdapter(List<OrderBean> options) {
        this.options = options;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pai_option, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String tv =options.get(position).getContent();
        holder.tvOption.setText(tv);

        holder.tvOption.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    ProblemInfoActivity.mDragStartListener.onStartDrag(holder);
                }
                return false;
            }

        });
    }


    @Override
    public int getItemCount() {
        return options.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_pai)
        public TextView tvOption;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
