package net.onest.timestoryprj.adapter.problem;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tinytongtong.tinyutils.LogUtils;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.problem.ProblemInfoActivity;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.customview.CusLinearLayout;
import net.onest.timestoryprj.customview.LinkLineView;
import net.onest.timestoryprj.entity.LinkDataBean;
import net.onest.timestoryprj.entity.Problem;
import net.onest.timestoryprj.entity.problem.OrderBean;
import net.onest.timestoryprj.entity.problem.ProblemLinkLine;
import net.onest.timestoryprj.entity.problem.ProblemSelect;
import net.onest.timestoryprj.entity.problem.ProblemgetOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class ProblemInfoListAdapter extends RecyclerView.Adapter<ProblemInfoListAdapter.MyHolder> {

    Context context;
    List<Problem> problems;
    private ProblemSelect problemSelect;
    private ProblemLinkLine problemLinkLine;
    private ProblemgetOrder problemgetOrder;



    public ProblemInfoListAdapter(Context context, List<Problem> problems) {
        this.context = context;
        this.problems = problems;
//        if(null!=Constant.userProblems){
//            Constant.userProblems.clear();
//        }else {
//            Constant.userProblems = new ArrayList<>();
//        }

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_problem_info, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Problem problem = problems.get(position);
        String[] contents1 = problem.getProblemContent().split(Constant.DELIMITER);
        switch (problem.getProblemType()) {
            case 1://选
                LogUtils.d("adapter测试收到的"+problem.toString());
                holder.linearLayouts.get(1).setVisibility(View.INVISIBLE);
                holder.linearLayouts.get(2).setVisibility(View.INVISIBLE);
                problemSelect = new ProblemSelect();
                problemSelect.setProblemId(problem.getProblemId());
                problemSelect.setDynastyId(problem.getDynastyId());
                problemSelect.setProblemType(1);
                problemSelect.setTitle(contents1[0]);
                problemSelect.setOptionA(contents1[1]);
                problemSelect.setOptionApic(contents1[2]);
                problemSelect.setOptionB(contents1[3]);
                problemSelect.setOptionBpic(contents1[4]);
                problemSelect.setOptionC(contents1[5]);
                problemSelect.setOptionCpic(contents1[6]);
                problemSelect.setOptionD(contents1[7]);
                problemSelect.setOptionDpic(contents1[8]);
                problemSelect.setProblemKey(problem.getProblemKey());
                problemSelect.setProblemDetails(problem.getProblemDetails());
                LogUtils.d("测试收到的选择题",problemSelect.toString());
//                Constant.userProblems.add(problemSelect);
//               展示数据
                holder.problemTitles[0].setText(problemSelect.getTitle());
                holder.tvOptionsXuan[0].setText(problemSelect.getOptionA());
                holder.tvOptionsXuan[1].setText(problemSelect.getOptionB());
                holder.tvOptionsXuan[2].setText(problemSelect.getOptionC());
                holder.tvOptionsXuan[3].setText(problemSelect.getOptionD());
                String url = ServiceConfig.SERVICE_ROOT + "/picture/download/";
                Glide.with(context).load(url + problemSelect.getOptionApic()).into(holder.ivOptionsXuan[0]);
                Glide.with(context).load(url + problemSelect.getOptionBpic()).into(holder.ivOptionsXuan[1]);
                Glide.with(context).load(url + problemSelect.getOptionCpic()).into(holder.ivOptionsXuan[2]);
                Glide.with(context).load(url + problemSelect.getOptionDpic()).into(holder.ivOptionsXuan[3]);
//                点击liner 跳转activity 携带数据
                holder.linearLayouts.get(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        jumpInfoActivity(1,position);
                    }
                });

                break;
            case 2://连
                holder.linearLayouts.get(2).setVisibility(View.INVISIBLE);
                holder.linearLayouts.get(0).setVisibility(View.INVISIBLE);
//                展示数据
                problemLinkLine  = new ProblemLinkLine();
                problemLinkLine.setDynastyId(problem.getDynastyId());
                problemLinkLine.setProblemId(problem.getProblemId());
                problemLinkLine.setTitle(contents1[0]);
                problemLinkLine.setOptionA(contents1[1]);
                problemLinkLine.setOptionB(contents1[2]);
                problemLinkLine.setOptionC(contents1[3]);
                problemLinkLine.setOptionD(contents1[4]);
                problemLinkLine.setOptionAdes(contents1[5]);
                problemLinkLine.setOptionBdes(contents1[6]);
                problemLinkLine.setOptionCdes(contents1[7]);
                problemLinkLine.setOptionDdes(contents1[8]);
                String problemKey = problem.getProblemKey();
                problemLinkLine.setProblemKey(problemKey);
                problemLinkLine.setProblemType(2);
                problemLinkLine.setProblemDetails(problem.getProblemDetails());

                LogUtils.d("原始排序题",problemLinkLine.toString());
//                Constant.userProblems.add(problemLinkLine);
                String[] qNum = problemKey.split(Constant.DELIMITER);
                List<LinkDataBean> linkDataBeans = new ArrayList<>();
                for(int i=0;i<4;i++){
                    LinkDataBean linkDataBean = new LinkDataBean();
                    linkDataBean.setContent(contents1[i+1]);
                    linkDataBean.setQ_num(Integer.parseInt(qNum[i]));
                    linkDataBean.setRow(i+1);
                    linkDataBean.setCol(0);
                    linkDataBeans.add(linkDataBean);
                }

                for(int i=0;i<4;i++){
                    LinkDataBean linkDataBean = new LinkDataBean();
                    linkDataBean.setContent(contents1[i+5]);
                    linkDataBean.setQ_num(Integer.parseInt(qNum[i+4]));
                    linkDataBean.setRow(i+1);
                    linkDataBean.setCol(1);
                    linkDataBeans.add(linkDataBean);
                }
                Log.e("linkDataBeans: ",linkDataBeans.size()+"" );
                holder.problemTitles[1].setText(problemLinkLine.getTitle());
                holder.linkLineView.setData(linkDataBeans);
//                点击liner 跳转activity 携带数据
                holder.linearLayouts.get(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, ProblemInfoActivity.class);
                        intent.putExtra("type", "lian");
                        intent.putExtra("dynastyId", problemLinkLine.getDynastyId());
                        intent.putExtra("problem", problemLinkLine);
                        intent.putExtra("linkDataBeans", (Serializable) linkDataBeans);
                        intent.putExtra("before", "info");
                        intent.putExtra("position",position);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        context.startActivity(intent);
                    }
                });
                break;
            case 3://排
                holder.linearLayouts.get(1).setVisibility(View.INVISIBLE);
                holder.linearLayouts.get(0).setVisibility(View.INVISIBLE);
//                展示数据
                problemgetOrder = new ProblemgetOrder();
                problemgetOrder.setTitle(contents1[0]);
                problemgetOrder.setProblemType(3);
                problemgetOrder.setDynastyId(problem.getDynastyId());
                problemgetOrder.setProblemKey(problem.getProblemKey());
                problemgetOrder.setProblemDetails(problem.getProblemDetails());
                String key = problem.getProblemKey();
                List<OrderBean> orderBeans = new ArrayList<>();
                for(int i=0;i<key.length();i++){
                    //
                    OrderBean orderBean = new OrderBean();
                    orderBean.setContent(contents1[i+1]);
                    orderBean.setOrder(Integer.parseInt(key.charAt(i)+""));
                    orderBeans.add(orderBean);
                }
                problemgetOrder.setContents(orderBeans);

//                Constant.userProblems.add(problemgetOrder);

                holder.problemTitles[2].setText(problemgetOrder.getTitle());
                int colnum = 2;
                if(problemgetOrder.getContents().size()>4)
                    colnum=3;
                if(problemgetOrder.getContents().size()>6)
                    colnum=4;

                holder.rePai.setLayoutManager(new GridLayoutManager(context, colnum));
//        rv.setLayoutManager(new LinearLayoutManager(this));
                OptionLianAdapter adapter = new OptionLianAdapter(problemgetOrder.getContents());
                holder.rePai.setAdapter(adapter);
                holder.rePai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        jumpInfoActivity(3,position);
                    }
                });
//                点击liner 跳转activity 携带数据
                holder.linearLayouts.get(2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        jumpInfoActivity(3,position);
                    }
                });
                break;
        }
    }

    private void jumpInfoActivity(int type,int position) {

        Intent intent = new Intent(context, ProblemInfoActivity.class);
        switch (type){
            case 1:
                intent.putExtra("type", "xuan");
                intent.putExtra("dynastyId", problemSelect.getDynastyId());
                intent.putExtra("problem", problemSelect);
                break;
            case 3:
                intent.putExtra("type", "pai");
                intent.putExtra("dynastyId", problemgetOrder.getDynastyId());
                intent.putExtra("problem", problemgetOrder);
                break;
        }
        intent.putExtra("position",position);
        intent.putExtra("before", "info");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return problems.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.type_xuan, R.id.type_lian, R.id.type_pai})
        List<CusLinearLayout> linearLayouts;

        @BindViews({R.id.problem_xuan_info_title, R.id.problem_lian_info_title, R.id.problem_pai_info_title})
        TextView[] problemTitles;

        //        选择题
        @BindViews({R.id.iv_optionA, R.id.iv_optionB, R.id.iv_optionC, R.id.iv_optionD})
        ImageView[] ivOptionsXuan;

        @BindViews({R.id.tv_optionA, R.id.tv_optionB, R.id.tv_optionC, R.id.tv_optionD})
        TextView[] tvOptionsXuan;

        @BindView(R.id.link_line_view)
        LinkLineView linkLineView;

        @BindView(R.id.re_pai)
        RecyclerView rePai;



        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            for(int i=0;i<linearLayouts.size();i++){
//                不能点击
                linearLayouts.get(i).setChildClickable(false);
            }

        }
    }
}
