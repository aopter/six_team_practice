package net.onest.timestoryprj.activity.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.book.BookDatailAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.BookVO;
import net.onest.timestoryprj.entity.CertificateUserBookListVO;
import net.onest.timestoryprj.entity.SpecificBookCompletedListVO;
import net.onest.timestoryprj.entity.donate.Book;
import net.onest.timestoryprj.util.ToastUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookDetailActivity extends AppCompatActivity {
    private List<SpecificBookCompletedListVO> bookListVOS = new ArrayList<>();
    private String url1 = "/book/details/";
    private String url2 = "/userbook/donated/";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.btn_start_donate)
    Button startDonate;
    private BookVO book;
    private TextView tvBookDes;
    private TextView tvBookName;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private PromptDialog promptDialog;
    private ImageView ivBookImg;
    private int bookId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    bookListVOS = (List<SpecificBookCompletedListVO>) msg.obj;
                    initAdapter();
                    promptDialog.dismissImmediately();
                    break;
                case 2:
                    book = (BookVO) msg.obj;
                    tvBookDes.setText("    " + book.getBookDes());
                    tvBookName.setText(book.getBookName());
                    Glide.with(getApplicationContext())
                            .load(ServiceConfig.SERVICE_ROOT + "/img/" + book.getBookPic())
                            .into(ivBookImg);
                    break;
                case 3:
                    String result1 = (String) msg.obj;
                    if (result1.contains("false")) {
                        //捐卡失败
                        ToastUtil.showSickToast(getApplicationContext(), "您已开启此公益项目，无须再次开启", 1500);
                    } else {
                        ToastUtil.showEncourageToast(getApplicationContext(), "开启此公益图书捐赠成功", 1500);
                    }
                    break;
            }
        }
    };

    private void initAdapter() {
        BookDatailAdapter bookDatailAdapter = new BookDatailAdapter(this, bookListVOS, R.layout.item_book);
        ListView bookList = findViewById(R.id.list_books);
        bookList.setAdapter(bookDatailAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        bookId = intent.getIntExtra("bookId", -1);
        if (bookId != -1) {
            promptDialog = new PromptDialog(this);
            //设置自定义属性
            promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(1000);
            promptDialog.showLoading("正在加载");
            initGson();
            initOkHttp();
            findViews();
            downLoadBookDetails();
            downLoadDonateDetails();
        } else {
            ToastUtil.showSickToast(getApplicationContext(), "获取图书详情错误", 1500);
        }
    }

    private void findViews() {
        tvBookDes = findViewById(R.id.tv_book_des);
        tvBookName = findViewById(R.id.tv_book_name);
        ivBookImg = findViewById(R.id.iv_book_img);
    }

    // 图书详情
    private void downLoadBookDetails() {
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + url1 + bookId)
                .build();
        Log.e("url", ServiceConfig.SERVICE_ROOT + url1 + bookId);
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            Response response = null;
            try {
                response = call.execute();
                String json = response.body().string();
                Log.e("data", json);
                //1.得到集合类型
                Type type = new TypeToken<BookVO>() {
                }.getType();
                BookVO book = gson.fromJson(json, type);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = book;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 捐赠记录
     */
    private void downLoadDonateDetails() {
        // /book/details/{bookId}
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + url2 + bookId)
                .build();
        Log.e("url", ServiceConfig.SERVICE_ROOT + url2 + bookId);
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            Response response = null;
            try {
                response = call.execute();
                String json = response.body().string();
                Log.e("datas", json);
                //1.得到集合类型
                Type type = new TypeToken<ArrayList<SpecificBookCompletedListVO>>() {
                }.getType();
                List<SpecificBookCompletedListVO> bookList = gson.fromJson(json, type);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = bookList;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 用户开启此公益项目
    @OnClick(R.id.btn_start_donate)
    void startDoanate() {
        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/userbook/addpro/" + Constant.User.getUserId() + "/" + bookId)
                        .build();
                Call call = okHttpClient.newCall(request);
                Log.e("url", ServiceConfig.SERVICE_ROOT + "/userbook/addpro/" + Constant.User.getUserId() + "/" + bookId);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        Log.e("result", json);
                        Message msg = new Message();
                        msg.what = 3;
                        msg.obj = json;
                        handler.sendMessage(msg);
                    }
                });
            }
        }.start();
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
