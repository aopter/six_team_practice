package net.onest.timestoryprj.adapter.problem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.problem.ProblemInfoActivity;
import net.onest.timestoryprj.entity.Problem;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProblemInfoListAdapter extends RecyclerView.Adapter<ProblemInfoListAdapter.MyHolder> {

    Context context;
    List<Problem> problems;


    public ProblemInfoListAdapter(Context context, List<Problem> problems) {
        this.context = context;
        this.problems = problems;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_problem_info,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return  myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Problem problem = problems.get(position);

        switch (problem.getProblemType()){
            case 1://选
                holder.linearLayouts.get(1).setVisibility(View.INVISIBLE);
                holder.linearLayouts.get(2).setVisibility(View.INVISIBLE);
//                点击liner 跳转activity 携带数据
                holder.linearLayouts.get(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProblemInfoActivity.class);
                        intent.putExtra("type","xuan");
                        intent.putExtra("problem",problem);
                        intent.putExtra("before","info");

                        context.startActivity(intent);
                    }
                });

                break;
            case 2://连
                holder.linearLayouts.get(2).setVisibility(View.INVISIBLE);
                holder.linearLayouts.get(0).setVisibility(View.INVISIBLE);

//                点击liner 跳转activity 携带数据
                holder.linearLayouts.get(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProblemInfoActivity.class);
                        intent.putExtra("type","lian");
                        intent.putExtra("problem",problem);
                        context.startActivity(intent);

                    }
                });
                break;
            case 3://排
                holder.linearLayouts.get(1).setVisibility(View.INVISIBLE);
                holder.linearLayouts.get(0).setVisibility(View.INVISIBLE);

//                点击liner 跳转activity 携带数据
                holder.linearLayouts.get(2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProblemInfoActivity.class);
                        intent.putExtra("type","pai");
                        intent.putExtra("problem",problem);
                        context.startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return problems.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.type_xuan,R.id.type_lian,R.id.type_pai})
        List<LinearLayout> linearLayouts;

        @BindViews({R.id.problem_xuan_info_title,R.id.problem_lian_info_title,R.id.problem_pai_info_title})
        List<TextView> problemTitles;

        @BindView(R.id.iv_optionA)
        ImageView ivOptionA;
        @BindView(R.id.iv_optionB)
        ImageView ivOptionB;
        @BindView(R.id.iv_optionC)
        ImageView ivOptionC;
        @BindView(R.id.iv_optionD)
        ImageView ivOptionD;


        @BindView(R.id.tv_optionA)
        TextView tvOptionA;
        @BindView(R.id.tv_optionB)
        TextView tvOptionB;
        @BindView(R.id.tv_optionC)
        TextView tvOptionC;
        @BindView(R.id.tv_optionD)
        TextView tvOptionD;

//        //连线
//        @BindView(R.id.lian_first)
//        LinearLayout llLianFirst;
//        @BindView(R.id.lian_second)
//        LinearLayout llLianSecond;
//        @BindView(R.id.lian_third)
//        LinearLayout llLianThird;
//        @BindView(R.id.lian_forth)
//        LinearLayout llLianForth;
//
//        @BindViews({R.id.tv1_lian_first,R.id.tv2_lian_first,R.id.tv1_lian_second,R.id.tv2_lian_second
//        ,R.id.tv1_lian_third,R.id.tv2_lian_third,R.id.tv1_lian_forth,R.id.tv2_lian_forth})
//        List<TextView> listLianTextViews;

//        排序题
        @BindViews({R.id.tv_pai_first,R.id.tv_pai_second,R.id.tv_pai_third,R.id.tv_pai_forth})
        List<TextView> listPaiTextViews;





        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
