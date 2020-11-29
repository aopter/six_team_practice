package net.onest.timestoryprj.activity.problem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.problem.OnStartDragListener;
import net.onest.timestoryprj.adapter.problem.OptionLianAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.customview.LinkLineView;
import net.onest.timestoryprj.dialog.problem.ProblemAnswerDialog;
import net.onest.timestoryprj.entity.LinkDataBean;
import net.onest.timestoryprj.entity.Problem;
import net.onest.timestoryprj.entity.problem.OrderBean;
import net.onest.timestoryprj.entity.problem.ProblemLinkLine;
import net.onest.timestoryprj.entity.problem.ProblemSelect;
import net.onest.timestoryprj.entity.problem.ProblemgetOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ProblemInfoActivity extends AppCompatActivity {


    private boolean isGetAnswer = false;
    private String dynastyId;
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

    @BindView(R.id.title_lian)
    TextView tvLianTitle;

    @BindView(R.id.problem_save)
    Button btnProblemSave;

    @BindView(R.id.problem_xuan_info_title)
    TextView tv_problem_xuan_info_title;

    @BindViews({R.id.iv_optionA, R.id.iv_optionB, R.id.iv_optionC, R.id.iv_optionD})
    ImageView[] ivOptionsXuan;

    @BindViews({R.id.tv_optionA, R.id.tv_optionB, R.id.tv_optionC, R.id.tv_optionD})
    TextView[] tvOptionsXuan;

    //   选择题选项的linear
    @BindViews({R.id.optionA, R.id.optionB, R.id.optionC, R.id.optionD})
    LinearLayout[] llOptionsXuan;

    @BindView(R.id.btn_pai_to_check)
    Button btnToCheck;

    //   三种题目
    Problem cProblem;
    ProblemSelect problemSelect;
    ProblemgetOrder problemgetOrder;
    ProblemLinkLine problemLinkLine;
    private int cType;

    private Handler handler = new Handler(
    ) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.arg1) {
                case 1://选择
                    ProblemSelect problemSelect = (ProblemSelect) msg.obj;
                    tv_problem_xuan_info_title.setText(problemSelect.getTitle());
                    tvOptionsXuan[0].setText(problemSelect.getOptionA());
                    tvOptionsXuan[1].setText(problemSelect.getOptionB());
                    tvOptionsXuan[2].setText(problemSelect.getOptionC());
                    tvOptionsXuan[3].setText(problemSelect.getOptionD());
                    String url = ServiceConfig.SERVICE_ROOT + "/picture/download/problem/";
                    Glide.with(ProblemInfoActivity.this).load(url + problemSelect.getOptionApic()).into(ivOptionsXuan[0]);
                    Glide.with(ProblemInfoActivity.this).load(url + problemSelect.getOptionBpic()).into(ivOptionsXuan[1]);
                    Glide.with(ProblemInfoActivity.this).load(url + problemSelect.getOptionCpic()).into(ivOptionsXuan[2]);
                    Glide.with(ProblemInfoActivity.this).load(url + problemSelect.getOptionDpic()).into(ivOptionsXuan[3]);
                    break;
                case 2://连线
                    tvLianTitle.setText(problemLinkLine.getTitle());
                    List<LinkDataBean> linkDataBeans = (List<LinkDataBean>) msg.obj;
                    linkLineView.setData(linkDataBeans);
                    linkLineView.setOnChoiceResultListener((correct, yourAnswer) -> {
                        // 结果
                        StringBuilder sb = new StringBuilder();
                        sb.append("正确与否：");
                        sb.append(correct);
                        sb.append("\n");
                        tvResult.setText(sb.toString());
//                        显示解析
                        btnAnswer.setVisibility(View.VISIBLE);

                    });
                    break;

                case 3://排序

//                    {"李白乘舟将欲行", "不及汪伦送我情", "忽闻岸上踏歌声", "桃花潭水深千尺"};
                    rePai.setLayoutManager(new GridLayoutManager(ProblemInfoActivity.this, 3));
//        rv.setLayoutManager(new LinearLayoutManager(this));
                    OptionLianAdapter adapter = new OptionLianAdapter(problemgetOrder.getContents());
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
                            int fromPosition = viewHolder.getAdapterPosition();
                            int toPosition = target.getAdapterPosition();

//                            Collections.swap(problemgetOrder.getContents(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
//                            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                            return onMove(fromPosition, toPosition);
                        }


                        public boolean onMove(int fromPosition, int toPosition) {
                            if (fromPosition < toPosition) {
                                //从上往下拖动，每滑动一个item，都将list中的item向下交换，向上滑同理。
                                for (int i = fromPosition; i < toPosition; i++) {
                                    Collections.swap(problemgetOrder.getContents(), i, i + 1);
                                }
                            } else {
                                for (int i = fromPosition; i > toPosition; i--) {
                                    Collections.swap(problemgetOrder.getContents(), i, i - 1);
                                }
                            }
                            adapter.notifyItemMoved(fromPosition, toPosition);
                            //原因下面会说明
                            return true;
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
                case 4://收藏
                    btnProblemSave.setText("已收藏");
                    Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                    break;
                case 5://取消收藏
                    btnProblemSave.setText("收藏");
                    Toast.makeText(getApplicationContext(), "取消收藏成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public static OnStartDragListener mDragStartListener;
    //    okhttp客户端类
    private OkHttpClient okHttpClient;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_info);
        ButterKnife.bind(this);
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        cProblem = new Problem();

        Glide.with(this).load(R.mipmap.deng).into(ivGif);
        Intent intent = getIntent();

        String type = intent.getStringExtra("type");
//        Problem problem = (Problem) intent.getSerializableExtra("problem");
        dynastyId = intent.getStringExtra("dynastyId");
//        Log.e("朝代id: ", dynastyId);
        String before = intent.getStringExtra("before");

        if (before.equals("types"))//类型页
        {
            btnAnswer.setVisibility(View.INVISIBLE);
        }
        if (null != type) {
            switch (type) {
                case "xuan":
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    if(before.equals("info")){
                        problemSelect  = (ProblemSelect) intent.getSerializableExtra("problem");
                        cProblem.setProblemId(problemSelect.getProblemId());
                        cProblem.setProblemDetails(problemSelect.getProblemDetails());
//                        cProblem
                        //跳转显示
                        Message message = new Message();
                        message.arg1 = 1;
                        message.obj = problemSelect;//发送选择题
                        handler.sendMessage(message);
                        return;
                    }
//                   请求题目
                    cType = 1;
                    getProblem(1);
                    break;
                case "lian":
                    cType = 2;
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    if(before.equals("info")){
                        problemLinkLine  = (ProblemLinkLine) intent.getSerializableExtra("problem");
                        cProblem.setProblemId(problemLinkLine.getProblemId());
                        cProblem.setProblemDetails(problemLinkLine.getProblemDetails());

                        List<LinkDataBean> linkDataBeans = (List<LinkDataBean>) intent.getSerializableExtra("linkDataBeans");

                        //跳转显示
                        Message message = new Message();
                        message.arg1 = 2;
                        message.obj = linkDataBeans;//发送选择题
                        handler.sendMessage(message);
                        return;
                    }
                    getProblem(2);
                    break;
                case "all":
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    break;
                case "pai":
                    cType = 3;
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    llTypeLian.setVisibility(View.INVISIBLE);
                    if(before.equals("info")){
                        problemgetOrder  = (ProblemgetOrder) intent.getSerializableExtra("problem");
                        cProblem.setProblemId(problemgetOrder.getProblemId());
                        cProblem.setProblemDetails(problemgetOrder.getProblemDetails());


                        //跳转显示
                        Message message = new Message();
                        message.arg1 = 3;
                        message.obj = problemgetOrder;//发送选择题
                        handler.sendMessage(message);
                        return;
                    }
                    getProblem(3);

                    break;
            }
        }
    }

    /**
     * 请求题目
     *
     * @param type
     */
    private void getProblem(int type) {
        isGetAnswer = false;
        String url = ServiceConfig.SERVICE_ROOT + "/problem/replenish/" + type + "/" + dynastyId + "";
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
//        请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure: ", "下载排行榜失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Problem problem = gson.fromJson(jsonData, Problem.class);
                cProblem = problem;
                Log.e("problemgson: ", problem.toString());
                String[] contents1 = problem.getProblemContent().split(Constant.DELIMITER);
                switch (type) {
                    case 1:
//                        problemSelect.dynastyId  = dynastyId;
                        problemSelect = new ProblemSelect();
                        problemSelect.setProblemId(problem.getProblemId());
                        problemSelect.setTitle(contents1[0]);
                        problemSelect.setOptionA(contents1[1]);
                        problemSelect.setOptionApic(contents1[2]);
                        problemSelect.setOptionB(contents1[3]);
                        problemSelect.setOptionBpic(contents1[4]);
                        problemSelect.setOptionC(contents1[5]);
                        problemSelect.setOptionCpic(contents1[6]);
                        problemSelect.setOptionD(contents1[7]);
                        problemSelect.setOptionDpic(contents1[8]);
                        problemSelect.setAnswer(problem.getProblemKey());
                        problemSelect.setDetails(problem.getProblemDetails());
                        Log.e("GetProblemDetails:", problem.getProblemDetails());
//                        发送消息
                        Message message = new Message();
                        message.arg1 = 1;
                        message.obj = problemSelect;//发送选择题
                        handler.sendMessage(message);
                        break;
                    case 2://连线题
                        //        初始化连线题

                        problemLinkLine = new ProblemLinkLine();
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
                        String[] qNum = problemKey.split(Constant.DELIMITER);

                        List<LinkDataBean> linkDataBeans = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            LinkDataBean linkDataBean = new LinkDataBean();
                            linkDataBean.setContent(contents1[i + 1]);
                            linkDataBean.setQ_num(Integer.parseInt(qNum[i]));
                            linkDataBean.setRow(i + 1);
                            linkDataBean.setCol(0);
                            linkDataBeans.add(linkDataBean);
                        }

                        for (int i = 0; i < 4; i++) {
                            LinkDataBean linkDataBean = new LinkDataBean();
                            linkDataBean.setContent(contents1[i + 5]);
                            linkDataBean.setQ_num(Integer.parseInt(qNum[i + 4]));
                            linkDataBean.setRow(i + 1);
                            linkDataBean.setCol(1);
                            linkDataBeans.add(linkDataBean);
                        }
                        Message msg = new Message();
                        msg.arg1 = 2;
                        msg.obj = linkDataBeans;
                        handler.sendMessage(msg);
                        break;
                    case 3:
                        problemgetOrder = new ProblemgetOrder();
                        problemgetOrder.setTitle(contents1[0]);
                        String key = problem.getProblemKey();
                        Log.e("onResponse答案: ", key);
                        List<OrderBean> orderBeans = new ArrayList<>();
                        for (int i = 0; i < key.length(); i++) {
                            //
                            OrderBean orderBean = new OrderBean();
                            orderBean.setContent(contents1[i + 1]);
                            orderBean.setOrder(Integer.parseInt(key.charAt(i) + ""));
                            orderBeans.add(orderBean);
                        }
                        problemgetOrder.setContents(orderBeans);
                        Log.e("onResponse: ", cProblem.toString());
                        Message message1 = new Message();
                        message1.arg1 = 3;
                        handler.sendMessage(message1);


                        break;
                }
            }
        });
//
    }


    //    选择题点击事件
    @OnClick({R.id.problem_save, R.id.problem_answer,
            R.id.optionA, R.id.optionB, R.id.optionC, R.id.optionD, R.id.btn_pai_to_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.problem_save:
                Log.e("当先题目: ", cProblem.toString());
                String s = btnProblemSave.getText().toString();
                if (s.equals("收藏")) {//取消收藏
//                    /userproblem/collect/{userId}/{dynastyId}/{problemId}
                    String urlSaveProblem = ServiceConfig.SERVICE_ROOT + "/userproblem/collect/" +
                            1 + "/" + dynastyId + "/" + cProblem.getProblemId() + "";
                    Log.e("urlSaveProblem: ", urlSaveProblem);
                    Request.Builder builder = new Request.Builder();
                    builder.url(urlSaveProblem);
                    //构造请求类
                    Request request = builder.build();
                    final Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("onFailure: ", "收藏失败");
                            //稍后再试
                            //
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String data = response.body().string();
                            Log.e("onResponse: ", data);
                            Message message = new Message();
                            message.arg1 = 4;
                            handler.sendMessage(message);
                        }
                    });
                } else {
//                    /userproblem/uncollect/{userId}/{dynastyId}/{problemId}
                    String urlSaveProblem = ServiceConfig.SERVICE_ROOT + "/userproblem/uncollect/" +
                            1 + "/" + dynastyId + "/" + cProblem.getProblemId() + "";
                    Request.Builder builder = new Request.Builder();
                    builder.url(urlSaveProblem);
                    //构造请求类
                    Request request = builder.build();
                    final Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("onFailure: ", "取消收藏失败");
                            //稍后再试
                            //
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
//                            String data = response.body().toString();
//                            Log.e("onResponse: ", data);
                            Message message = new Message();
                            message.arg1 = 5;
                            handler.sendMessage(message);
                        }
                    });
                    btnProblemSave.setText("收藏");

                }
                break;
            case R.id.problem_answer:
                getProblemAnswer();
                break;
            case R.id.optionA:
//                判断正误的方法
                checkUserXuanAnswer(problemSelect.getOptionA());
                break;
            case R.id.optionB:
                checkUserXuanAnswer(problemSelect.getOptionB());
                break;
            case R.id.optionC:
                checkUserXuanAnswer(problemSelect.getOptionC());

                break;
            case R.id.optionD:
                checkUserXuanAnswer(problemSelect.getOptionD());
                break;
            case R.id.btn_pai_to_check:
                Log.e("onViewClicked: ", isGetAnswer + "");
                if (isGetAnswer) {
                    return;
                }
//                检查题目
                List<OrderBean> orderBeans = problemgetOrder.getContents();
                isGetAnswer = true;
                for (int i = 0; i < orderBeans.size(); i++) {

//                    检查
                    int postion = i + 1;
                    if (orderBeans.get(i).getOrder() != postion) {
                        //错误
                        Toast.makeText(getApplicationContext(), "回答错误", Toast.LENGTH_SHORT).show();
                        btnAnswer.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), "回答正确", Toast.LENGTH_SHORT).show();
                btnAnswer.setVisibility(View.VISIBLE);
                break;
        }
    }

    //    检查正误
    private void checkUserXuanAnswer(String option) {
        if (problemSelect.getAnswer().equals(option) && !isGetAnswer) {
            isGetAnswer = true;
//          正确 加积分加经验
            Toast.makeText(getApplicationContext(), "回答正确", Toast.LENGTH_SHORT).show();
//          /problem/answer/{userId}/{dynastyId}/{problemId}/{result}
            //显示查看解析
            btnAnswer.setVisibility(View.VISIBLE);
        } else {
            isGetAnswer = true;
            Toast.makeText(getApplicationContext(), "回答错误", Toast.LENGTH_SHORT).show();
            btnAnswer.setVisibility(View.VISIBLE);
        }
    }

//    选择题点击事件
//@OnClick({R.id.optionA,R.id.optionB,R.id.optionC,R.id.optionD})


    /**
     * 显示答案弹窗
     */
    private void getProblemAnswer() {
        ProblemAnswerDialog dialog = new ProblemAnswerDialog(this);
        dialog.setDetail(cProblem.getProblemDetails());
        dialog.show();
    }


}
