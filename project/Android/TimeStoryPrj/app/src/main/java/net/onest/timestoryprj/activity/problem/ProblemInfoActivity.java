package net.onest.timestoryprj.activity.problem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tinytongtong.tinyutils.LogUtils;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.problem.OnStartDragListener;
import net.onest.timestoryprj.adapter.problem.OptionLianAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.customview.LinkLineView;
import net.onest.timestoryprj.dialog.problem.ProblemAnswerDialog;
import net.onest.timestoryprj.entity.LinkDataBean;
import net.onest.timestoryprj.entity.Problem;
import net.onest.timestoryprj.entity.UserUnlockDynasty;
import net.onest.timestoryprj.entity.problem.OrderBean;
import net.onest.timestoryprj.entity.problem.ProblemCheckAnswer;
import net.onest.timestoryprj.entity.problem.ProblemLinkLine;
import net.onest.timestoryprj.entity.problem.ProblemSelect;
import net.onest.timestoryprj.entity.problem.ProblemgetOrder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;
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

    @BindViews({R.id.iv_optionA_check, R.id.iv_optionB_check, R.id.iv_optionC_check, R.id.iv_optionD_check})
    ImageView[] imageViewsCkeck;

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

    @BindView(R.id.re_container)
    RelativeLayout reContainer;

    //   选择题选项的linear
    @BindViews({R.id.optionA, R.id.optionB, R.id.optionC, R.id.optionD})
    LinearLayout[] llOptionsXuan;

    @BindView(R.id.btn_pai_to_check)
    Button btnToCheck;

    @BindView(R.id.tv_pai_title)
    TextView tvPaiTitle;


    //   三种题目
    Problem cProblem;
    ProblemSelect problemSelect;
    ProblemgetOrder problemgetOrder;
    ProblemLinkLine problemLinkLine;
    private int cType;
    private int cIndex = 0;//当前索引
    private PromptDialog promptDialog;

    //    左右屏幕的滑动事件
    final int RIGHT = 0;
    final int LEFT = 1;
    private GestureDetector gestureDetector;

    private List<Problem> myProblems;

    private Animation animationin;
    private Animation animationout;
    private String before;


    private Handler handler = new Handler(
    ) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.arg1) {
                case 1://选择
                    btnToCheck.setVisibility(View.GONE);
                    tv_problem_xuan_info_title.setText(problemSelect.getTitle());
                    tvOptionsXuan[0].setText(problemSelect.getOptionA());
                    tvOptionsXuan[1].setText(problemSelect.getOptionB());
                    tvOptionsXuan[2].setText(problemSelect.getOptionC());
                    tvOptionsXuan[3].setText(problemSelect.getOptionD());
                    String url = ServiceConfig.SERVICE_ROOT + "/img/";
                    LogUtils.d(url + problemSelect.getOptionApic());
                    Glide.with(getApplicationContext()).load(url + problemSelect.getOptionApic()).into(ivOptionsXuan[0]);
                    Glide.with(getApplicationContext()).load(url + problemSelect.getOptionBpic()).into(ivOptionsXuan[1]);
                    Glide.with(getApplicationContext()).load(url + problemSelect.getOptionCpic()).into(ivOptionsXuan[2]);
                    Glide.with(getApplicationContext()).load(url + problemSelect.getOptionDpic()).into(ivOptionsXuan[3]);
                    break;
                case 2://连线
                    btnToCheck.setVisibility(View.GONE);
//                    linkLineView = new LinkLineView(ProblemInfoActivity.this);
                    tvLianTitle.setText(problemLinkLine.getTitle());
                    List<LinkDataBean> linkDataBeans = (List<LinkDataBean>) msg.obj;
                    // 先清除掉原有绘制的线
                    linkLineView.removeAllViews();
//                    LinkLineView linkView = getLinkView();
                    Log.e("handleMessage收到: ", problemLinkLine.toString());
                    linkLineView.setData(linkDataBeans);
                    linkLineView.setOnChoiceResultListener((correct, yourAnswer) -> {
                        if (correct) {
                            UpProblemAnwer(problemLinkLine.getProblemId(), 1);
                        } else {
                            UpProblemAnwer(problemLinkLine.getProblemId(), 2);
                        }

                    });
                    break;
                case 3://排序
                    btnToCheck.setVisibility(View.VISIBLE);
                    tvPaiTitle.setText(problemgetOrder.getTitle());
//                    {"李白乘舟将欲行", "不及汪伦送我情", "忽闻岸上踏歌声", "桃花潭水深千尺"};
                    int colnum = 2;
                    if (problemgetOrder.getContents().size() > 4)
                        colnum = 3;
                    if (problemgetOrder.getContents().size() > 6)
                        colnum = 4;
                    rePai.setLayoutManager(new GridLayoutManager(ProblemInfoActivity.this, colnum));
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
                    promptDialog.showSuccess("收藏成功");
                    break;
                case 5://取消收藏
                    btnProblemSave.setText("收藏");
                    promptDialog.showSuccess("取消收藏成功");
                    break;
                case 6://是否收藏
                    String isCollection = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(isCollection);
                        String result = jsonObject.getString("result");
                        if (result.equals("true")) {
                            btnProblemSave.setText("已收藏");
                        } else {
                            btnProblemSave.setText("收藏");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 7://上传结果
                    String re = (String) msg.obj;
                    ProblemCheckAnswer problemCheckAnswer = gson.fromJson(re, ProblemCheckAnswer.class);

                    LogUtils.d("结果", problemCheckAnswer.toString());
                    Constant.User.setUserExperience(problemCheckAnswer.getUserExperience());
                    Constant.User.setUserCount(problemCheckAnswer.getUserCount());
                    if (problemCheckAnswer.getUnlock().equals("true")) {
                        //成功
                        UserUnlockDynasty userUnlockDynasty = new UserUnlockDynasty();
                        userUnlockDynasty.setUserId(Constant.User.getUserId());
                        userUnlockDynasty.setDynastyId(dynastyId);
                        userUnlockDynasty.setDynastyName("秦朝");//
                        Constant.UnlockDynasty.add(userUnlockDynasty);
                    }
                    break;
                case 8:
                    for (int i = 0; i < 4; ++i) {//清空图片
                        imageViewsCkeck[i].setImageResource(0);
                    }
                    break;
                case 9:
                    promptDialog.dismissImmediately();
                    break;
                case  10:
                    promptDialog.dismissImmediately();
                    Toast.makeText(getApplicationContext(),"题目正在录入哦，先去唐朝看看吧~",Toast.LENGTH_SHORT);
                    break;
            }
        }
    };

    public static OnStartDragListener mDragStartListener;
    //    okhttp客户端类
    private OkHttpClient okHttpClient;
    private Gson gson;

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 0) {
                        doResult(RIGHT);
                    } else if (x < 0) {
                        doResult(LEFT);
                    }
                    return true;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_info);
        ButterKnife.bind(this);

        promptDialog = new PromptDialog(this);
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(3000);
//        动画
        animationin = AnimationUtils.loadAnimation(
                ProblemInfoActivity.this, R.anim.anim_in_right);

        animationout = AnimationUtils.loadAnimation(
                ProblemInfoActivity.this, R.anim.anim_in_left_problem);
//        左右触屏
        gestureDetector = new GestureDetector(ProblemInfoActivity.this, onGestureListener);
        myProblems = new ArrayList<>();
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        cProblem = new Problem();

        Glide.with(this).load(R.mipmap.deng).into(ivGif);
        Intent intent = getIntent();

        String type = intent.getStringExtra("type");
//        Problem problem = (Problem) intent.getSerializableExtra("problem");
        dynastyId = intent.getStringExtra("dynastyId");
//        Log.e("朝代id: ", dynastyId);
        before = intent.getStringExtra("before");

        if (before.equals("types"))//类型页
        {
            btnAnswer.setVisibility(View.INVISIBLE);//解析
        }
        if (null != type) {
            switch (type) {
                case "xuan":
                    cType = 1;
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    if (before.equals("info")) {//收藏页
                        btnProblemSave.setText("已收藏");
                        cIndex = intent.getIntExtra("position", 0);
                        problemSelect = (ProblemSelect) intent.getSerializableExtra("problem");
                        cProblem.setProblemId(problemSelect.getProblemId());
                        cProblem.setProblemDetails(problemSelect.getProblemDetails());
//                        cProblem
                        //跳转显示
                        Message message = new Message();
                        message.arg1 = 1;
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
                    if (before.equals("info")) {
                        btnProblemSave.setText("已收藏");
                        cIndex = intent.getIntExtra("position", 0);
                        problemLinkLine = (ProblemLinkLine) intent.getSerializableExtra("problem");
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
                    cType = 4;
                    getRandomProblem();
                    break;
                case "pai":
                    cType = 3;
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    llTypeLian.setVisibility(View.INVISIBLE);
                    if (before.equals("info")) {
                        btnProblemSave.setText("已收藏");
                        cIndex = intent.getIntExtra("position", 0);
                        problemgetOrder = (ProblemgetOrder) intent.getSerializableExtra("problem");
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

    private void getRandomProblem() {
        //                   产生随机数
        int min = 1;
        int max = 3;
        int num = getMyRandom(min, max);
        llTypeLian.setVisibility(View.VISIBLE);
        llTypePai.setVisibility(View.VISIBLE);
        llTypeXuan.setVisibility(View.VISIBLE);

        Log.e("随机数: ", num + "");
        switch (num) {
            case 1://选择题
                llTypeLian.setVisibility(View.INVISIBLE);
                llTypePai.setVisibility(View.INVISIBLE);
                getProblem(1);
                break;
            case 2:
                llTypePai.setVisibility(View.INVISIBLE);
                llTypeXuan.setVisibility(View.INVISIBLE);
                getProblem(2);
//                            连线
                break;
            case 3://排序
                llTypeLian.setVisibility(View.INVISIBLE);
                llTypeXuan.setVisibility(View.INVISIBLE);
                getProblem(3);
                break;
        }
    }

    private int getMyRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 请求题目
     *
     * @param type
     */
    private void getProblem(int type) {
        btnAnswer.setVisibility(View.INVISIBLE);
        LogUtils.d("长度suoyou", myProblems.size() + "");
        isGetAnswer = false;
        String url = ServiceConfig.SERVICE_ROOT + "/problem/replenish/" + type + "/" + dynastyId + "";
        LogUtils.d("请求题目路径", url);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        promptDialog.showLoading("正在加载");
//        请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

//                promptDialog.dismissImmediately();
//                promptDialog.showError("网络较差，请稍后");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msgDis = new Message();
                msgDis.arg1 = 9;
                handler.sendMessage(msgDis);
                String jsonData = response.body().string();
                Problem problem = gson.fromJson(jsonData, Problem.class);
                LogUtils.d("收到的题目", problem.toString());
                if (0 == problem.getProblemId()) {
                    Message msgNull = new Message();
                    msgDis.arg1 = 10;
                    handler.sendMessage(msgNull);
                    return;
                }
//                查看是否已经收藏
                checkIsUserCollection(problem.getProblemId());
                cProblem = problem;
                String[] contents1 = problem.getProblemContent().split(Constant.DELIMITER);
                switch (type) {
                    case 1:
                        Message msgIv = new Message();
                        msgIv.arg1 = 8;
                        handler.sendMessage(msgIv);
                        problemSelect = new ProblemSelect();
                        problemSelect.setDynastyId(dynastyId);
                        problemSelect.setProblemType(1);
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
                        problemSelect.setProblemKey(problem.getProblemKey());
                        problemSelect.setProblemDetails(problem.getProblemDetails());
                        myProblems.add(problemSelect);

//                        发送消息
                        Message message = new Message();
                        message.arg1 = 1;
                        handler.sendMessage(message);
                        break;
                    case 2://连线题
                        //        初始化连线题
                        problemLinkLine = new ProblemLinkLine();
                        problemLinkLine.setDynastyId(dynastyId);
                        problemLinkLine.setProblemId(problem.getProblemId());
                        problemLinkLine.setProblemDetails(problem.getProblemDetails());
                        problemLinkLine.setProblemType(2);
                        problemLinkLine.setTitle(contents1[0]);
                        problemLinkLine.setOptionA(contents1[1]);
                        problemLinkLine.setOptionB(contents1[2]);
                        problemLinkLine.setOptionC(contents1[3]);
                        problemLinkLine.setOptionD(contents1[4]);
                        problemLinkLine.setOptionAdes(contents1[5]);
                        problemLinkLine.setOptionBdes(contents1[6]);
                        problemLinkLine.setOptionCdes(contents1[7]);
                        problemLinkLine.setOptionDdes(contents1[8]);
                        problemLinkLine.setProblemKey(problem.getProblemKey());
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
                        myProblems.add(problemLinkLine);
                        Message msg = new Message();
                        msg.arg1 = 2;
                        msg.obj = linkDataBeans;
                        handler.sendMessage(msg);
                        break;
                    case 3:
                        problemgetOrder = new ProblemgetOrder();
                        problemgetOrder.setProblemDetails(problem.getProblemDetails());
                        problemgetOrder.setDynastyId(dynastyId);
                        problemgetOrder.setTitle(contents1[0]);
                        problemgetOrder.setProblemType(3);
                        problemgetOrder.setProblemKey(problem.getProblemKey());
                        problemgetOrder.setProblemId(problem.getProblemId());

                        String key = problem.getProblemKey();
                        List<OrderBean> orderBeans = new ArrayList<>();
                        for (int i = 0; i < key.length(); i++) {
                            //
                            OrderBean orderBean = new OrderBean();
                            orderBean.setContent(contents1[i + 1]);
                            orderBean.setOrder(Integer.parseInt(key.charAt(i) + ""));
                            orderBeans.add(orderBean);
                        }
                        problemgetOrder.setContents(orderBeans);
                        myProblems.add(problemgetOrder);
                        Message message1 = new Message();
                        message1.arg1 = 3;
                        handler.sendMessage(message1);
                        break;
                }
            }
        });
//
    }

    /**
     * 查看是否已经收藏
     *
     * @param problemId
     */
    private void checkIsUserCollection(int problemId) {
        String url = ServiceConfig.SERVICE_ROOT + "/userproblem/iscollection/" + Constant.User.getUserId() + "/" + dynastyId + "/" + problemId;
        LogUtils.d("查看是否已经收藏", url);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
//        请求
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String string = response.body().string();
                Message message = new Message();
                message.arg1 = 6;
                message.obj = string;
                handler.sendMessage(message);

            }
        });
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
                    String urlSaveProblem = ServiceConfig.SERVICE_ROOT + "/userproblem/collect/" +
                            Constant.User.getUserId() + "/" + dynastyId + "/" + cProblem.getProblemId() + "";
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
                            promptDialog.showError("网络较差，请稍后");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String data = response.body().string();
                            Message message = new Message();
                            message.arg1 = 4;
                            handler.sendMessage(message);
                        }
                    });
                } else {
//                    /userproblem/uncollect/{userId}/{dynastyId}/{problemId}
                    String urlSaveProblem = ServiceConfig.SERVICE_ROOT + "/userproblem/uncollect/" +
                            Constant.User.getUserId() + "/" + dynastyId + "/" + cProblem.getProblemId() + "";

                    LogUtils.d("取消收藏路径", urlSaveProblem);
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
                            promptDialog.showError("网络较差，请稍后");

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Message message = new Message();
                            message.arg1 = 5;
                            handler.sendMessage(message);
                        }
                    });


                }
                break;
            case R.id.problem_answer:
                getProblemAnswer();
                break;
            case R.id.optionA:
//                判断正误的方法
                boolean b = checkUserXuanAnswer(problemSelect.getOptionA());
                if (b) {
                    imageViewsCkeck[0].setImageResource(R.mipmap.p_yes);
                } else {
                    //显示错误 正确显示正确
                    getCheckImage(0);
                }

                break;
            case R.id.optionB:
                boolean b2 = checkUserXuanAnswer(problemSelect.getOptionB());
                if (b2) {
                    imageViewsCkeck[1].setImageResource(R.mipmap.p_yes);
                } else {
                    //显示错误 正确显示正确
                    getCheckImage(1);
                }
                break;
            case R.id.optionC:
                boolean b3 = checkUserXuanAnswer(problemSelect.getOptionC());
                if (b3) {
                    imageViewsCkeck[2].setImageResource(R.mipmap.p_yes);
                } else {
                    //显示错误 正确显示正确
                    getCheckImage(2);
                }

                break;
            case R.id.optionD:
                boolean b4 = checkUserXuanAnswer(problemSelect.getOptionD());
                if (b4) {
                    imageViewsCkeck[3].setImageResource(R.mipmap.p_yes);
                } else {
                    //显示错误 正确显示正确
                    getCheckImage(3);
                }
                break;
            case R.id.btn_pai_to_check:
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
                        UpProblemAnwer(problemgetOrder.getProblemId(), 2);

                        //Toast.makeText(getApplicationContext(), "回答错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                UpProblemAnwer(problemgetOrder.getProblemId(), 1);
                break;
        }
    }

    private void getCheckImage(int index) {
        imageViewsCkeck[index].setImageResource(R.mipmap.p_no);
        if (problemSelect.getProblemKey().equals(problemSelect.getOptionA())) {
            imageViewsCkeck[0].setImageResource(R.mipmap.p_yes);
        }
        if (problemSelect.getProblemKey().equals(problemSelect.getOptionB())) {
            imageViewsCkeck[1].setImageResource(R.mipmap.p_yes);
        }
        if (problemSelect.getProblemKey().equals(problemSelect.getOptionC())) {
            imageViewsCkeck[2].setImageResource(R.mipmap.p_yes);
        }
        if (problemSelect.getProblemKey().equals(problemSelect.getOptionD())) {
            imageViewsCkeck[3].setImageResource(R.mipmap.p_yes);
        }


    }

    //    检查正误
    private boolean checkUserXuanAnswer(String option) {
        if (isGetAnswer) {
            return false;
        }
        if (problemSelect.getProblemKey().equals(option) && !isGetAnswer) {
            UpProblemAnwer(problemSelect.getProblemId(), 1);
            isGetAnswer = true;
            return true;
        } else {
            UpProblemAnwer(problemSelect.getProblemId(), 2);
            isGetAnswer = true;
            return false;
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


    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    //手势滑动
    private void doResult(int action) {
        LogUtils.d("长度", myProblems.size() + "");
        switch (action) {
            case LEFT:
                cIndex += 1;
                if (before.equals("info")) {
                    return;
                }
                if (myProblems.size() > cIndex) {//下一道
                    getBeforeOrNextProblem();//显示
                    animationin.setDuration(600);
                    reContainer.startAnimation(animationin);
                    return;
                } else {
                    LogUtils.d("获取题目");
                    LogUtils.d(cType + "aaa");
                    if (cType == 4) {//随机
                        getRandomProblem();
                    } else {
                        getProblem(cProblem.getProblemType());
                    }
                }
                animationin.setDuration(600);
                reContainer.startAnimation(animationin);
                break;
            case RIGHT:
                cIndex -= 1;
                if (before.equals("info")) {
                    return;
                }
                if (cIndex < 0) {//上一道
//                    提示不能滑动
                    cIndex = 0;
                    Toast.makeText(getApplicationContext(), "不能再滑啦", Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    显示
                    getBeforeOrNextProblem();//显示上一道题目
                }
                animationout.setDuration(600);
                reContainer.startAnimation(animationout);
                break;
        }
    }

    /**
     * 获取上一个 or 下一个题目
     */
    private void getBeforeOrNextProblem() {
        LogUtils.d("类型", "");
        Problem problem = myProblems.get(cIndex);
        switch (problem.getProblemType()) {
            case 1://选择
                Message msgIv = new Message();
                msgIv.arg1 = 8;
                handler.sendMessage(msgIv);
                if (cType == 4) {
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    llTypeXuan.setVisibility(View.VISIBLE);
                }
                problemSelect = (ProblemSelect) problem;
                Message message = new Message();
                message.arg1 = 1;
                handler.sendMessage(message);
                break;
            case 2:
                if (cType == 4) {
                    llTypeLian.setVisibility(View.VISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    llTypeXuan.setVisibility(View.INVISIBLE);
                } else {
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypeLian.setVisibility(View.VISIBLE);
                }

                problemLinkLine = (ProblemLinkLine) problem;
                String[] qNum = problemLinkLine.getProblemKey().split(Constant.DELIMITER);

                List<LinkDataBean> linkDataBeans = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    LinkDataBean linkDataBean = new LinkDataBean();
                    switch (i) {
                        case 0:
                            linkDataBean.setContent(problemLinkLine.getOptionA());
                            break;
                        case 1:
                            linkDataBean.setContent(problemLinkLine.getOptionB());
                            break;
                        case 2:
                            linkDataBean.setContent(problemLinkLine.getOptionC());
                            break;
                        case 3:
                            linkDataBean.setContent(problemLinkLine.getOptionD());
                            break;
                    }
                    linkDataBean.setQ_num(Integer.parseInt(qNum[i]));
                    linkDataBean.setRow(i + 1);
                    linkDataBean.setCol(0);
                    linkDataBeans.add(linkDataBean);
                }

                for (int i = 0; i < 4; i++) {
                    LinkDataBean linkDataBean = new LinkDataBean();
                    switch (i) {
                        case 0:
                            linkDataBean.setContent(problemLinkLine.getOptionAdes());
                            break;
                        case 1:
                            linkDataBean.setContent(problemLinkLine.getOptionBdes());
                            break;
                        case 2:
                            linkDataBean.setContent(problemLinkLine.getOptionCdes());
                            break;
                        case 3:
                            linkDataBean.setContent(problemLinkLine.getOptionDdes());
                            break;
                    }
                    linkDataBean.setQ_num(Integer.parseInt(qNum[i + 4]));
                    linkDataBean.setRow(i + 1);
                    linkDataBean.setCol(1);
                    linkDataBeans.add(linkDataBean);
                }

                Message message2 = new Message();
                message2.arg1 = 2;
                message2.obj = linkDataBeans;
                handler.sendMessage(message2);
                break;
            case 3:
                if (cType == 4) {
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.VISIBLE);
                    llTypeXuan.setVisibility(View.INVISIBLE);
                }
                problemgetOrder = (ProblemgetOrder) problem;
                Message message1 = new Message();
                message1.arg1 = 3;
                handler.sendMessage(message1);
                break;
        }
    }

    /**
     * 结算
     */

    public void UpProblemAnwer(int problemId, int result) {
///problem/answer/{userId}/{dynastyId}/{problemId}/{result}
//备注：result中1，表示正确，2表示错误
        btnAnswer.setVisibility(View.VISIBLE);
        switch (result) {
            case 1:
                promptDialog.showInfo("恭喜你，回答正确喽！");
                break;
            case 2:
                promptDialog.showInfo("很遗憾，回答错误~");
                break;
        }
        if (before.equals("info")) {
            return;//收藏页不上传
        }
//        上传服务器
//        /problem/answer/{userId}/{dynastyId}/{problemId}/{result}
        String url = ServiceConfig.SERVICE_ROOT + "/problem/answer/" +
                Constant.User.getUserId() + "/" + dynastyId + "/" + problemId + "/" + result;
        Log.e("urlSaveProblem: ", url);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String string = response.body().string();
                Message message = new Message();
                message.arg1 = 7;
                message.obj = string;
                handler.sendMessage(message);

            }
        });


    }


}

