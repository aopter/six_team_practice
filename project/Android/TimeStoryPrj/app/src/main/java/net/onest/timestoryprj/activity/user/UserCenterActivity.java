package net.onest.timestoryprj.activity.user;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tinytongtong.tinyutils.LogUtils;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.card.DrawCardActivity;
import net.onest.timestoryprj.activity.card.MyCardActivity;
import net.onest.timestoryprj.activity.dynasty.HomepageActivity;
import net.onest.timestoryprj.activity.problem.ProblemCollectionActivity;
import net.onest.timestoryprj.adapter.user.HistoryTodayAdapter;
import net.onest.timestoryprj.adapter.user.UserRankListAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.HistoryDay;
import net.onest.timestoryprj.entity.User;
import net.onest.timestoryprj.entity.UserStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserCenterActivity extends AppCompatActivity {
    //    okhttp客户端类
    private OkHttpClient okHttpClient;
    //    历史上的今天 文本框
    private TextView tvData;

    @BindView(R.id.btn_set)
    Button btnSet;

    @BindView(R.id.btn_plus)
    Button btnPlus;

    @BindView(R.id.tv_point)
    public TextView tvPoint;

    @BindView(R.id.tv_level)
    public TextView tvLevel;

    @BindView(R.id.iv_header)
    public ImageView ivHeader;

    @BindView(R.id.lv_user_list)
    public ListView rankList;


    @BindView(R.id.re_his_today)
    RecyclerView recyclerView;

    @BindView(R.id.btn_go_dynasty)
    Button btnGoDynasty;
    @BindView(R.id.btn_my_card)
    Button btnMyCards;

    @BindView(R.id.btn_my_collections)
    Button btnMyCollections;

    @BindView(R.id.btn_card)
    Button btnGetCard;
    Gson gson;

    @BindView(R.id.experience_progress)
    ProgressBar progressBar;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.arg1) {
                case 1:
                    //加载完毕
                    HistoryTodayAdapter historyTodayAdapter = new HistoryTodayAdapter(UserCenterActivity.this);
//                    配置
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserCenterActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(historyTodayAdapter);
                    break;
                case 2://排行榜
//                    加载数据到adapter
                    UserRankListAdapter cakeListAdapter = new UserRankListAdapter(UserCenterActivity.this, Constant.UserRankList,
                            R.layout.item_user_rank_list);
                    rankList.setAdapter(cakeListAdapter);
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_center);
//      绑定初始化ButterKnife
        ButterKnife.bind(this);
        showUserInfo();

        gson = new Gson();
        okHttpClient = new OkHttpClient();
//  //获取历史上的今天
        getHistoryToday();
//        获取排行榜
        getUserRank();
//        initListView();
    }


    /**
     * 初始化进度条
     */
    private void initProgress() {
        long userExperience = Constant.User.getUserExperience();
        UserStatus userStatus = Constant.User.getUserStatus();
        long experMax = userStatus.getStatusExperienceTop();
        long experMin = userStatus.getStatusExperienceLow();
        long experOnStatus = experMax - experMin;
        DecimalFormat df = new DecimalFormat("0.00");
        String rate = df.format((float) (userExperience - experMin) / experOnStatus);
        double exRate = Double.parseDouble(rate);
        int progress = (int) (exRate * 100);
        progressBar.setProgress(progress);
    }

    //加载头像
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadImgWithPlaceHolders() {
//        .skipMemoryCache(true)//跳过内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//不缓冲disk硬盘中
        //头像
        if (Constant.User.getFlag() == 0){
            if (Constant.User.getUserHeader() == null){
                Glide.with(this)
                        .load(R.mipmap.man)
                        .circleCrop()
                        .into(ivHeader);
            }else {
                Glide.with(this)
                        .load(ServiceConfig.SERVICE_ROOT + "/img/" + Constant.User.getUserHeader())
                        .circleCrop()
                        .into(ivHeader);
            }
        }else if (Constant.User.getFlag() == 1){
            Glide.with(this)
                    .load(Constant.User.getUserHeader())
                    .circleCrop()
                    .into(ivHeader);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showUserInfo() {
        tvLevel.setText(Constant.User.getUserStatus().getStatusName());
        loadImgWithPlaceHolders();
        tvPoint.setText(Constant.User.getUserCount() + "");
        initProgress();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        showUserInfo();
    }

    /**
     * 获取用户排行榜列表
     */
    private void getUserRank() {
        Request.Builder builder = new Request.Builder();
        builder.url(ServiceConfig.SERVICE_ROOT + "/user/list");
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure: ", "下载排行榜失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Constant.UserRankList = gson.fromJson(jsonData, new TypeToken<List<User>>() {
                }.getType());
                Message message = new Message();
                message.arg1 = 2;
                handler.sendMessage(message);

            }
        });
//        加载地位
        if(Constant.userStatuses.size() < 1) {
            //请求
            Request.Builder builder1 = new Request.Builder();
            builder1.url(ServiceConfig.SERVICE_ROOT + "/status/list");
            //构造请求类
            Request request1 = builder1.build();
            final Call call1 = okHttpClient.newCall(request1);
            call1.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.d("下载失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonData = response.body().string();
                    Constant.userStatuses = gson.fromJson(jsonData, new TypeToken<List<UserStatus>>() {
                    }.getType());
                }
            });
        }


    }

    //获取历史上的今天
    private void getHistoryToday() {

        Request.Builder builder = new Request.Builder();
        Time t = new Time("GMT+8"); // 设置Time Zone资料。
        t.setToNow(); // 获得当前系统时间。
        int month = t.month + 1; //月份前面加1，是因为从0开始计算，需要加1操作
        int day = t.monthDay;
//        获取当前月份
//        获取当前时间
        builder.url(ServiceConfig.HISTORY_TODAY + "?v=1.0&month=" + month + "&day=" + day + "&key=7a9cf9c5a9ff6338f5484d484ba51587");
        LogUtils.d(ServiceConfig.HISTORY_TODAY + "?v=1.0&month=" + month + "&day=" + day + "&key=7a9cf9c5a9ff6338f5484d484ba51587");
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                LogUtils.d("历史上请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject his = jsonArray.getJSONObject(i);
                        HistoryDay historyDay = new HistoryDay();
                        historyDay.setDes(his.getString("des"));
                        historyDay.setTitle(his.getString("title"));
                        historyDay.setLunar(his.getString("lunar"));
                        historyDay.setYear(his.getInt("year"));
                        Constant.historyDays.add(historyDay);
                    }
                    Message message = handler.obtainMessage();
                    message.arg1 = 1;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick(R.id.btn_plus)
    void toRechargePage() {
        Intent intent = new Intent(getApplicationContext(), RechargeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_go_dynasty)
    public void jumpDynasty() {
        Intent intent = new Intent(UserCenterActivity.this, HomepageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_my_card)
    public void jumpMyCard() {
        Intent intent = new Intent(UserCenterActivity.this, MyCardActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.btn_my_collections, R.id.btn_card, R.id.btn_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_my_collections:
                Intent intent = new Intent(UserCenterActivity.this, ProblemCollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_card:
                Intent intent2 = new Intent(UserCenterActivity.this, DrawCardActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_set:
                Intent intent3 = new Intent(UserCenterActivity.this, SettingActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
