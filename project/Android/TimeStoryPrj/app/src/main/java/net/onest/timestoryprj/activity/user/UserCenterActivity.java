package net.onest.timestoryprj.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.onest.timestoryprj.R;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserCenterActivity extends AppCompatActivity {

    //    okhttp客户端类
    private OkHttpClient okHttpClient;
    //    历史上的今天 文本框
    private TextView tvData;

    @BindView(R.id.tv_name)
    public TextView tvName;

    @BindView(R.id.tv_level)
    public TextView tvLevel;

    @BindView(R.id.iv_header)
    public ImageView ivHeader;

    @BindView(R.id.lv_user_list)
    public ListView rankList;


    @BindView(R.id.tv_history_title)
    TextView tvHistoryTitle;
    @BindView(R.id.tv_history_time)
    TextView tvHistoryTime;
    @BindView(R.id.tv_history_context)
    TextView tvHistoryContext;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.arg1){
                case 1:
                    //加载完毕
               for (int i = 0; i < Constant.historyDays.size(); i++) {
                   HistoryDay historyDay = Constant.historyDays.get(i);
                   Log.e("changdu: ", Constant.historyDays.get(i).toString());
                   tvHistoryTitle.setText(historyDay.getTitle());
                   tvHistoryTime.setText(historyDay.getLunar()+"");
                   tvHistoryContext.setText(historyDay.getDes());
                    break;
                }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_center);
//      绑定初始化ButterKnife
        ButterKnife.bind(this);

        Glide.with(this)
                .load((R.mipmap.man))
                .circleCrop()
                .into(ivHeader);
//        tvData = findViewById(R.id.tv_data);
//  //获取历史上的今天
        getHistoryToday();
        initListView();
    }

    //获取历史上的今天
    private void getHistoryToday() {
        okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(ServiceConfig.HISTORY_TODAY + "?v=1.0&month=11&day=20&key=7a9cf9c5a9ff6338f5484d484ba51587");
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        //开启线程接收
        new Thread(() -> {
            try {
                Response response = call.execute();// 同步请求
                String jsonData = response.body().string();
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
//                for (int i = 0; i < Constant.historyDays.size(); i++) {
////                    Log.e("changdu: ", Constant.historyDays.get(i).toString());
////                }
////                Log.e("run: ", "执行完");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void initListView() {
//        造假数据
        List<User> userList = new ArrayList<>();
        for(int i=0;i<7;++i){
            User user = new User();
            UserStatus userStatus = new UserStatus();
            userStatus.setStatusName("童生");
            user.setUserStatus(userStatus);
            userList.add(user);
        }
        UserRankListAdapter cakeListAdapter = new UserRankListAdapter(this,userList ,
                R.layout.item_user_rank_list);
        rankList.setAdapter(cakeListAdapter);

    }
}
