package net.onest.timestoryprj.activity.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import net.onest.timestoryprj.entity.CertificateUserBookListVO;
import net.onest.timestoryprj.entity.SearchIncident;
import net.onest.timestoryprj.entity.donate.Book;

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

public class BookDetailActivity extends AppCompatActivity {
    private List<CertificateUserBookListVO> bookListVOS = new ArrayList<>();
    private String url1 = "/book/details/";
    private String url2 = "/userbook/donatedlist/";
    @BindView(R.id.back)
    ImageView back;
    private Book book;
    private TextView tvBookDes;
    private TextView tvBookName;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private PromptDialog promptDialog;
    private ImageView ivBookImg;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    bookListVOS = (List<CertificateUserBookListVO>) msg.obj;
                    initAdapter();
                    promptDialog.dismissImmediately();
                    break;
                case 2:
                    book = (Book) msg.obj;
                    tvBookDes.setText(book.getBookDes());
                    tvBookName.setText(book.getBookName());
//                    Glide.with(getApplicationContext())
//                            .load(ServiceConfig.SERVICE_ROOT + "/img/" + book.getBookPic())
//                            .into(ivBookImg);
                    break;
            }
        }
    };

    private void initAdapter() {
        BookDatailAdapter bookDatailAdapter = new BookDatailAdapter(this,bookListVOS,R.layout.item_book);
        ListView bookList = findViewById(R.id.list_books);
        bookList.setAdapter(bookDatailAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ButterKnife.bind(this);
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(1000);
        promptDialog.showLoading("正在加载");
        initGson();
        initOkHttp();
        Intent intent = getIntent();
        String bookId = intent.getStringExtra("book_id");
        findViews();
        downLoadBookDetails();
        downLoadDonateDetails(bookId);

    }

    private void findViews() {
        tvBookDes = findViewById(R.id.tv_book_des);
        tvBookName = findViewById(R.id.tv_book_name);
        ivBookImg = findViewById(R.id.iv_book_img);
    }

    private void downLoadBookDetails() {
        // /userbook/donatedlist/{userId}
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + url2 + Constant.User.getUserId())
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(()->{
            Response response = null;
            try {
                response = call.execute();
                String json = response.body().string();
                //1.得到集合类型
                Type type = new TypeToken<Book>(){
                }.getType();
//                Book book= gson.fromJson(json, type);
                Book book = new Book();
                book.setBookName("一千零一夜");
                book.setBookDes("很好的一本书");
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
     * 下载书籍详情
     */
    private void downLoadDonateDetails(String bookId) {
        // /book/details/{bookId}
        Request request = new Request.Builder()
                .url(ServiceConfig.SERVICE_ROOT + url1 + bookId)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(()->{
            Response response = null;
            try {
                response = call.execute();
                String json = response.body().string();
                //1.得到集合类型
                Type type = new TypeToken<List<CertificateUserBookListVO>>(){
                }.getType();
//                List<CertificateUserBookListVO> bookList = gson.fromJson(json, type);
                List<CertificateUserBookListVO> bookList = new ArrayList<>();
                CertificateUserBookListVO cerBook = new CertificateUserBookListVO();
                cerBook.setDonateObject("希望小学");
                cerBook.setDonateTime("2000年10月1日");
                for (int i = 0; i< 10; i++){
                    bookList.add(cerBook);
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
