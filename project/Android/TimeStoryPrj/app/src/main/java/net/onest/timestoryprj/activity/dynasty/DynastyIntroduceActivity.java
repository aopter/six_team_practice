package net.onest.timestoryprj.activity.dynasty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.problem.SelectProblemTypeActivity;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.dialog.card.CustomDialog;
import net.onest.timestoryprj.entity.Dynasty;
import net.onest.timestoryprj.util.TextUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynastyIntroduceActivity extends AppCompatActivity {

    @BindView(R.id.btn_pre)
    Button btnPre;
    private RelativeLayout rlBack;
    private ImageView ivHead;
    private TextView tvDynastyName;
    private TextView tvDynastyIntro;
    private Button btnImg;
    private Button btnQuestions;
    private Button btnDetails;
    private String dynastyId;
    private String DYNASTY_INFO = "/dynasty/details/";
    private Gson gson;
    private TextUtil textUtil;
    private Dynasty dynasty1;
    //    private ScrollView svIntroWord;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    dynasty1 = (Dynasty) msg.obj;
                    AssetManager assets = getAssets();
                    final Typeface typeface = Typeface.createFromAsset(assets, "fonts/custom_fontt.ttf");
                    final Typeface typeface1 = Typeface.createFromAsset(assets, "fonts/custom_font.ttf");
                    tvDynastyName.setTypeface(typeface);
                    tvDynastyIntro.setTypeface(typeface1);
                    tvDynastyName.setText(dynasty1.getDynastyName());
//                    tvDynastyIntro.setText(dynasty1.getDynastyInfo());
                    textUtil = new TextUtil(tvDynastyIntro, dynasty1.getDynastyInfo(), 100);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynasty_introduce);
        findViews();
        ButterKnife.bind(this);
        initGson();
        initData();
        setListener();
        Glide.with(this)
                .asGif()
                .load(R.mipmap.bird)
                .into(ivHead);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        btnDetails.setOnClickListener(myListener);
        btnQuestions.setOnClickListener(myListener);
        rlBack.setOnClickListener(myListener);
        btnImg.setOnClickListener(myListener);
        tvDynastyIntro.setOnClickListener(myListener);
    }

    /**
     * 初始化Gson
     */
    private void initGson() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .setDateFormat("yy:mm:dd")
                .create();
    }

    private void initData() {
        Intent intent = getIntent();
        dynastyId = intent.getStringExtra("dynastyId");
        downloadDynastyIntro(dynastyId);
    }

    /**
     * 从服务器获取朝代简介
     */
    private void downloadDynastyIntro(final String id) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ServiceConfig.SERVICE_ROOT + DYNASTY_INFO + id);
                    url.openStream();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String json = reader.readLine();
                    Dynasty dynasty = gson.fromJson(json, Dynasty.class);
                    Message msg = new Message();
                    msg.obj = dynasty;
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void findViews() {
        tvDynastyIntro = findViewById(R.id.tv_dynasty_intro);
        tvDynastyIntro.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvDynastyIntro.setClickable(true);
        tvDynastyName = findViewById(R.id.tv_dynasty_name);
        btnQuestions = findViewById(R.id.btn_questions);
        btnDetails = findViewById(R.id.btn_details);
        rlBack = findViewById(R.id.rl_back);
        btnImg = findViewById(R.id.btn_img);
        ivHead = findViewById(R.id.iv_head);
    }

    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            switch (view.getId()) {
                case R.id.btn_questions:
                    if(!dynastyId.equals("11")){
                        showIncidentDialog();
                        return;
                    }
                    Intent intent = new Intent(DynastyIntroduceActivity.this, SelectProblemTypeActivity.class);
                    intent.putExtra("dynastyId1", dynastyId);//朝代
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    break;
                case R.id.btn_details:
                    if(!dynastyId.equals("11")){
                        showIncidentDialog();
                        return;
                    }
                    Intent intent1 = new Intent();
                    intent1.setClass(DynastyIntroduceActivity.this, DetailsEventActivity.class);
                    intent1.putExtra("dynastyName1", tvDynastyName.getText());
                    intent1.putExtra("dynastyId1", dynastyId);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    break;
                case R.id.rl_back:
                    if (dynasty1 != null) {
                        if (textUtil.isFlag() == false) {
                            Log.e("false", "false");
                            textUtil.setFlag(true);
                        } else {
                            Log.e("true", "true");
                            tvDynastyIntro.setText(dynasty1.getDynastyInfo());
                        }
                    }
                    break;
                case R.id.tv_dynasty_intro:
                    Log.e("点击", "TextView");
                    if (dynasty1 != null) {
                        if (textUtil.isFlag() == false) {
                            Log.e("false", "false");
                            textUtil.setFlag(true);
                        } else {
                            Log.e("true", "true");
                            tvDynastyIntro.setText(dynasty1.getDynastyInfo());
                        }
                    }
                    break;
                case R.id.btn_img:
                    if (dynasty1 != null) {
                        Intent intent2 = new Intent();
                        intent2.setClass(DynastyIntroduceActivity.this, AllScreenImgActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                    }
                    break;


            }
        }
    }

    @OnClick(R.id.btn_pre)
    void backToLastPage() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textUtil != null) {
            if (textUtil.isFlag() == false) {
                Log.e("onresume", "flag:false");
                tvDynastyIntro.setText(dynasty1.getDynastyInfo());
                textUtil.setFlag(true);
            } else {
                Log.e("onresume", "flag:true");
                tvDynastyIntro.setText(dynasty1.getDynastyInfo());
            }
        }
    }




    /**
     * 没有题目和事件时，显示弹窗
     * 这次简略用!id=11判断
     */
    private void showIncidentDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("正在开发中");
        builder.setMessage("此朝代模块正在开发中，先去看看唐朝吧！");
        CustomDialog customDialog = builder.create();
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
        builder.setButtonConfirm("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null == textUtil) {
            return;
        } else {
            if (textUtil.isFlag() == false) {
                Log.e("false", "false");
                textUtil.setFlag(true);
            } else {
                Log.e("true", "true");
                tvDynastyIntro.setText(dynasty1.getDynastyInfo());
            }
        }
    }
}
