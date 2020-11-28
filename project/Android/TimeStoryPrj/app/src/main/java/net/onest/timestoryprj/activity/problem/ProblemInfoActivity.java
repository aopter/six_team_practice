package net.onest.timestoryprj.activity.problem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.problem.OnStartDragListener;
import net.onest.timestoryprj.adapter.problem.OptionLianAdapter;
import net.onest.timestoryprj.customview.LinkLineView;
import net.onest.timestoryprj.dialog.problem.ProblemAnswerDialog;
import net.onest.timestoryprj.dialog.user.CustomDialog;
import net.onest.timestoryprj.entity.LinkDataBean;
import net.onest.timestoryprj.entity.Problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProblemInfoActivity extends AppCompatActivity {


    @BindView(R.id.link_line_view)
    LinkLineView linkLineView;
    @BindView(R.id.fl_link_line)
    FrameLayout flLinkLine;
    @BindView(R.id.tv_result)
    TextView tvResult;

    @BindView(R.id.type_xuan)
    LinearLayout llTypeXuan;
    @BindView(R.id.type_pai)
    LinearLayout llTypePai;
    @BindView(R.id.type_lian)
    LinearLayout llTypeLian;
    LinearLayout llTypeAll;

    @BindView(R.id.re_pai)
    RecyclerView rePai;
    @BindView(R.id.iv_gif_bg)
    ImageView ivGif;


    @BindView(R.id.problem_answer)
    Button btnAnswer;

    @BindView(R.id.problem_save)
    Button btnProblemSave;

    public static OnStartDragListener mDragStartListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_info);
        ButterKnife.bind(this);

        Glide.with(this).load(R.mipmap.deng).into(ivGif);
        Intent intent = getIntent();

        String type = intent.getStringExtra("type");
        Problem problem = (Problem) intent.getSerializableExtra("problem");

        String before = intent.getStringExtra("before");
        if (before.equals("types"))
            btnAnswer.setVisibility(View.INVISIBLE);
        if (null != problem)
            Log.e("onCreate: ", problem.getProblemType() + "");
        if (null != type) {
            switch (type) {
                case "xuan":
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    break;
                case "lian":
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    List<LinkDataBean> list = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        LinkDataBean linkDataBean = new LinkDataBean();
                        if (i == 0) {
                            linkDataBean.setContent("王伟");
                            linkDataBean.setQ_num(3);
                        }
                        if (i == 1) {
                            linkDataBean.setContent("李玮玮");
                            linkDataBean.setQ_num(2);
                        }
                        if (i == 2) {
                            linkDataBean.setContent("王伟伟");
                            linkDataBean.setQ_num(1);
                        }
                        if (i == 3) {
                            linkDataBean.setContent("刘冠军");
                            linkDataBean.setQ_num(0);
                        }

                        linkDataBean.setRow(i);
                        linkDataBean.setCol(0);
                        list.add(linkDataBean);

                    }
                    for (int i = 0; i < 4; i++) {
                        LinkDataBean linkDataBean = new LinkDataBean();
                        if (i == 0) {
                            linkDataBean.setContent("android");
                            linkDataBean.setQ_num(2);
                        }
                        if (i == 1) {
                            linkDataBean.setContent("java");
                            linkDataBean.setQ_num(3);
                        }
                        if (i == 2) {
                            linkDataBean.setContent("h5");
                            linkDataBean.setQ_num(0);
                        }
                        if (i == 3) {
                            linkDataBean.setContent("aiii");
                            linkDataBean.setQ_num(1);
                        }
                        linkDataBean.setRow(i);

                        linkDataBean.setCol(1);
                        list.add(linkDataBean);
                    }


                    linkLineView.setData(list);
                    linkLineView.setOnChoiceResultListener((correct, yourAnswer) -> {
                        // 结果
                        StringBuilder sb = new StringBuilder();
                        sb.append("正确与否：");
                        sb.append(correct);
                        sb.append("\n");
                        tvResult.setText(sb.toString());
                    });

                    break;
                case "all":
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    break;
                case "pai":
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    llTypeLian.setVisibility(View.INVISIBLE);
                    List<String> options = new ArrayList<>();
//                    {"李白乘舟将欲行", "不及汪伦送我情", "忽闻岸上踏歌声", "桃花潭水深千尺"};
                    options.add("李白乘舟将欲行");
                    options.add("不及汪伦送我情");
                    options.add("忽闻岸上踏歌声");
                    options.add("桃花潭水深千尺");
                    options.add("穿棉衣穿棉衣啊");
                    options.add("打地鼠穿棉衣啊");

                    rePai.setLayoutManager(new GridLayoutManager(this, 3));
//        rv.setLayoutManager(new LinearLayoutManager(this));
                    OptionLianAdapter adapter = new OptionLianAdapter(options);
                    rePai.setAdapter(adapter);
                    //为RecycleView绑定触摸事件
                    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
                        @Override
                        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                            //首先回调的方法 返回int表示是否监听该方向
                            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//拖拽
                            int swipeFlags = 0;//侧滑删除
                            return makeMovementFlags(dragFlags, swipeFlags);
                        }

                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            //滑动事件
                            Log.e("getMovementFlags: ", "滑动");
                            Collections.swap(options, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                            return false;
                        }


                        @Override
                        public boolean isLongPressDragEnabled() {
                            //是否可拖拽
                            return true;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        }

                    });


                    mDragStartListener = new OnStartDragListener() {
                        @Override
                        public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                            helper.startDrag(viewHolder);
                        }
                    };

                    helper.attachToRecyclerView(rePai);


                    break;

            }
        }


    }

    @OnClick({R.id.problem_save,R.id.problem_answer})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.problem_save:
                String s = btnProblemSave.getText().toString();
                if(s.equals("收藏"))
                    btnProblemSave.setText("已收藏");
                else
                    btnProblemSave.setText("收藏");
                break;
            case R.id.problem_answer:
                getProblemAnswer();
                break;
        }
    }


    /**
     * 显示答案弹窗
     */
    private void getProblemAnswer() {
//

        ProblemAnswerDialog dialog = new ProblemAnswerDialog(this);
        dialog.show();
    }
}
