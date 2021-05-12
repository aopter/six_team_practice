package net.onest.timestoryprj.activity.benefit_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.benefit_shop.CustomDonateMyBookAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.donate.BookListVO;
import net.onest.timestoryprj.entity.donate.UserBookListVO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DonateProjectActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private GridView gvDonateMyBook;
    private List<UserBookListVO> userBookList;
    private PromptDialog promptDialog;
    private String url = "/userbook/donating/";
    private CustomDonateMyBookAdapter customDonateMyBookAdapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    userBookList = (List<UserBookListVO>) msg.obj;
                    initAdapter();
                    promptDialog.dismissImmediately();
                    break;
            }
        }
    };

    private void initAdapter() {
        customDonateMyBookAdapter = new CustomDonateMyBookAdapter(this, userBookList, R.layout.item_donate_mybook, this);
        gvDonateMyBook = findViewById(R.id.gv_donate_mybooks);
        gvDonateMyBook.setAdapter(customDonateMyBookAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_project);
        ButterKnife.bind(this);
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(1000);
        promptDialog.showLoading("正在加载");
        initGson();
        initOkHttp();
        downLoadMyProject();
    }

    /**
     * 从服务器中下载自己进行的项目
     */
    private void downLoadMyProject() {
        // /userbook/donating/{userId}
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + url + Constant.User.getUserId())
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(()->{
            Response response = null;
            try {
                response = call.execute();
                String json = response.body().string();
                Type type = new TypeToken<List<UserBookListVO>>(){}.getType();
//                List<UserBookListVO> myBookList = gson.fromJson(json,type);
                List<UserBookListVO> myBookList = new ArrayList<>();
                UserBookListVO ublv = new UserBookListVO();
                ublv.setProcess(80);
                BookListVO blv = new BookListVO();
                blv.setBookName("小王子");
                ublv.setBookListVO(blv);
                for (int i = 0; i < 10; i++){
                    myBookList.add(ublv);
                }
                Message msg = new Message();
                msg.what = 1;
                msg.obj = myBookList;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    /**
     * 初始化OKHTTP对象
     */
    private void initOkHttp() {
        okHttpClient = new OkHttpClient();
    }

    private void initGson() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("yy:mm:dd")
                .create();
    }

    @OnClick(R.id.back)
    void backToLastPage() {
        finish();
    }
}
