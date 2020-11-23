package net.onest.timestoryprj.activity.dynasty;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvLevel;
    private TextView tvPoint;
    private Button btnPlus;
    private Button btnCard;
    private Button btnMyCard;
    private Button btnMyCollections;
    private ImageView ivHeader;
    private Button btnVoice;
    private Button btnSettings;

    private HorizontalScrollView hsvDynasty;
    //定义MediaPlayer
    private MediaPlayer mediaPlayer;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        findViews();
        loadImgWithPlaceHolders();
        setListener();
        initMediaPlayer();
        initData();
    }

    /**
     * 初始化首页数据
     */
    private void initData() {

    }


    private void setListener() {
        MyListener myListener = new MyListener();
        btnVoice.setOnClickListener(myListener);
        btnSettings.setOnClickListener(myListener);
        btnMyCollections.setOnClickListener(myListener);
        btnMyCard.setOnClickListener(myListener);
        btnCard.setOnClickListener(myListener);
        btnPlus.setOnClickListener(myListener);
        ivHeader.setOnClickListener(myListener);
    }

    private void findViews() {
        ivHeader = findViewById(R.id.iv_header);
        btnVoice = findViewById(R.id.btn_voice);
        btnSettings = findViewById(R.id.btn_settings);
        tvName = findViewById(R.id.tv_name);
        tvLevel = findViewById(R.id.tv_level);
        tvPoint = findViewById(R.id.tv_point);
        btnPlus = findViewById(R.id.btn_plus);
        btnCard = findViewById(R.id.btn_card);
        btnMyCard = findViewById(R.id.btn_my_card);
        btnMyCollections = findViewById(R.id.btn_my_collections);
    }

    /**
     * 将头像剪为圆形
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadImgWithPlaceHolders() {
        Glide.with(this)
                .load(getDrawable(R.mipmap.man))
                .circleCrop()
                .into(ivHeader);
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_voice:
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        btnVoice.setBackgroundResource(R.mipmap.novoice);
                    }else{
                        mediaPlayer.start();
                        btnVoice.setBackgroundResource(R.mipmap.voice);
                    }
                    break;
                case R.id.btn_plus:
                    break;
                case R.id.btn_card:
                    break;
                case R.id.btn_my_card:
                    break;
                case R.id.btn_my_collections:
                    break;
                case R.id.btn_settings:
                    break;
                case R.id.iv_header:
                    break;
            }
        }
    }

    /**
     * 初始化背景音乐
     */
    private void initMediaPlayer() {
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
            mediaPlayer.start();
        }
    }
}
