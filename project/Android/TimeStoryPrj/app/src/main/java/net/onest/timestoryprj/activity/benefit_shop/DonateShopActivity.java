package net.onest.timestoryprj.activity.benefit_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.benefit_shop.CustomDonateBookAdapter;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.SearchIncident;
import net.onest.timestoryprj.entity.donate.BookListVO;

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

public class DonateShopActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    private Button btnCertification;
    private Button btnProject;
    private String url = "/book/list";
    private OkHttpClient okHttpClient;
    private Gson gson;
    private GridView gvDonateBook;
    private List<BookListVO> bookListVOS;
    private PromptDialog promptDialog;
    private CustomDonateBookAdapter customDonateBookAdapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    bookListVOS = (List<BookListVO>)msg.obj;
                    initAdapter();
                    promptDialog.dismissImmediately();
                    break;
            }
        }
    };

    private void initAdapter() {
        customDonateBookAdapter = new CustomDonateBookAdapter(this, bookListVOS, R.layout.item_donate_book, this);
        gvDonateBook = findViewById(R.id.gv_donate_books);
        gvDonateBook.setAdapter(customDonateBookAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_shop);
        ButterKnife.bind(this);
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(1000);
        promptDialog.showLoading("正在加载");
        findViews();
        initGson();
        initOkHttp();
        setListener();
        downBookListVo();
    }

    /**
     * 从服务端获取全部图书公益列表
     */
    private void downBookListVo() {
        // /book/list
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + url)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            Response response = null;
            try {
                response = call.execute();
                String json = response.body().string();
                //1.得到集合类型
                Type type = new TypeToken<List<SearchIncident>>(){
                }.getType();
//            List<BookListVO> bookList = gson.fromJson(json, type);
                List<BookListVO> bookList = new ArrayList<>();
                BookListVO bookListVO = new BookListVO();
                bookListVO.setBookName("一千零一夜");
                bookListVO.setBookId(1);
                bookListVO.setGoalNum(8);
                bookListVO.setTotalNum(5);
                for (int i = 0; i < 10; i++){
                    bookList.add(bookListVO);
                }
                Message msg = new Message();
                msg.what = 1;
                msg.obj = bookList;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void setListener() {
        MyListener myListener = new MyListener();
        btnProject.setOnClickListener(myListener);
        btnCertification.setOnClickListener(myListener);
    }

    private void findViews() {
        btnCertification = findViewById(R.id.btn_certification);
        btnProject = findViewById(R.id.btn_project);
    }

    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_certification:
                    Intent intent1 = new Intent(DonateShopActivity.this, CertificationActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    break;
                case R.id.btn_project:
                    Intent intent = new Intent(DonateShopActivity.this, DonateProjectActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    break;
            }
        }
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
