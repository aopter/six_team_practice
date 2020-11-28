package net.onest.timestoryprj.activity.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.dialog.user.CustomDialog;
import net.onest.timestoryprj.util.AudioUtil;

import java.text.DecimalFormat;

public class SettingActivity extends AppCompatActivity {

    private Button btnPerson;
    private Button btnVoice;
    private Button btnRule;
    private Button btnProblem;
    private Button btnExitLogin;
    private LinearLayout rightLayout;
    private ImageView ivVoice;
    private ImageView ivHeader;
    private String picturePath = "";//相册路径
    private Handler handler = new Handler(){

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViews();
        setListener();

    }

    private void setListener() {
        MyListener myListener = new MyListener();
        btnPerson.setOnClickListener(myListener);
        btnVoice.setOnClickListener(myListener);
        btnRule.setOnClickListener(myListener);
        btnProblem.setOnClickListener(myListener);
        btnExitLogin.setOnClickListener(myListener);
    }

    private void findViews() {
        btnPerson = findViewById(R.id.btn_person);
        btnVoice = findViewById(R.id.btn_voice_set);
        btnRule = findViewById(R.id.btn_rule);
        btnProblem = findViewById(R.id.btn_problem);
        btnExitLogin = findViewById(R.id.btn_exit_login);
        rightLayout = findViewById(R.id.rightlinear);
    }

    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_person://个人资料
                    rightLayout.removeAllViews();
                    setPersonAttr();
                    break;
                case R.id.btn_voice_set://音量设置
                    rightLayout.removeAllViews();
                    setVoiceAttr();
                    break;
                case R.id.btn_rule://查看规则
                    rightLayout.removeAllViews();
                    setRuleAttr();
                    break;
                case R.id.btn_problem://反馈问题
                    rightLayout.removeAllViews();
                    setProblemAttr();
                    break;
                case R.id.btn_exit_login://退出登录
                    rightLayout.removeAllViews();
                    setExitLoginAttr();
                    break;
            }
        }
    }

    /**
     * 退出登录布局
     */
    private void setExitLoginAttr() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CustomDialog dialog = new CustomDialog();
        if (!dialog.isAdded()){
            transaction.add(dialog,"dialog_tag");
        }
        transaction.show(dialog);
        transaction.commit();
    }

    /**
     * 反馈问题布局
     */
    private void setProblemAttr() {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(params);
        //问题编辑框
        EditText etProblem = new EditText(getApplicationContext());
        LinearLayout.LayoutParams etParam = new LinearLayout.LayoutParams(900,500);
        etParam.topMargin = 150;
        etParam.gravity = Gravity.CENTER_HORIZONTAL;
        etProblem.setText("说说你的问题吧……");
        etProblem.setTextSize(20);
        etProblem.setBackgroundResource(R.drawable.edit_style);
        etProblem.setLayoutParams(etParam);
        linearLayout.addView(etProblem);
        //提交
        Button btnSub = new Button(getApplicationContext());
        LinearLayout.LayoutParams btnParam = new LinearLayout.LayoutParams(280,160);
        btnSub.setText("提交");
        btnSub.setTextSize(20);
        btnSub.setBackgroundResource(R.color.ourDynastyRed);
        btnParam.gravity = Gravity.RIGHT;
        btnParam.rightMargin = 30;
        btnParam.topMargin = 120;
        btnSub.setLayoutParams(btnParam);
        linearLayout.addView(btnSub);
        rightLayout.addView(linearLayout);
        //提交按钮的监听器
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etProblem.getText().toString().trim();
                if (null != content && !content.equals("")){
                    Toast.makeText(getApplicationContext(),"您的问题已反馈成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"请填写您的问题",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 音量设置布局
     */
    private void setVoiceAttr() {
        //音量大小
        TextView tvTitle = new TextView(getApplicationContext());
        LinearLayout.LayoutParams tvParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParam.topMargin = 100;
        tvParam.leftMargin = 50;
        tvTitle.setText("音量大小");
        tvTitle.setTextSize(25);
        tvTitle.setLayoutParams(tvParam);
        rightLayout.addView(tvTitle);
        //声音
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 50;
        params.topMargin = 50;
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(params);
        ivVoice = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams ivParam = new LinearLayout.LayoutParams(150,150);
        ivParam.leftMargin = 20;
        ivVoice.setBackgroundResource(R.mipmap.voice);
        ivVoice.setLayoutParams(ivParam);
        linearLayout.addView(ivVoice);
        rightLayout.addView(linearLayout);

        SeekBar seekBar = new SeekBar(getApplicationContext());
        LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        par.topMargin = 10;
        seekBar.setLayoutParams(par);
        seekBar.setMax(100);
        seekBar.setProgress(30);
        linearLayout.addView(seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("进度",seekBar.getProgress()+"");
                int progress = seekBar.getProgress();
                AudioUtil audioUtil = AudioUtil.getInstance(getApplicationContext());
                int mediaMax = audioUtil.getMediaMaxVolume();
                Log.e("多媒体最大",mediaMax+"");
                int media = audioUtil.getMediaVolume();
                Log.e("多媒体当前",media+"");
                DecimalFormat df = new DecimalFormat("0.00");
                String stringVolume = df.format((float)progress/100*15);
                double volume = Double.parseDouble(stringVolume);
                Log.e("double volume",volume+"");
                int volume1 = 0;
                if (volume > 1){
                    volume1 = (int) Math.round(volume);
                    ivVoice.setBackgroundResource(R.mipmap.voice);
                }else if (volume > 0){
                    volume1 = (int) Math.ceil(volume);
                    ivVoice.setBackgroundResource(R.mipmap.voice);
                }else {
                    volume1 = 0;
                    ivVoice.setBackgroundResource(R.mipmap.novoice);
                }
                Log.e("int volume",volume1+"");
                audioUtil.setMediaVolume(volume1);
            }
        });

    }

    /**
     * 查看规则布局
     */
    private void setRuleAttr() {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(params);

        TextView tvTitle = new TextView(getApplicationContext());
        LinearLayout.LayoutParams tvParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParam.leftMargin = 50;
        tvParam.topMargin = 60;
        tvTitle.setText("时光序平台积分规则:");
        tvTitle.setTextSize(25);
        tvTitle.setLayoutParams(tvParam);
        linearLayout.addView(tvTitle);

        TextView tvRule = new TextView(getApplicationContext());
        tvRule.setText("规则");
        tvRule.setTextSize(20);
        LinearLayout.LayoutParams ruleParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ruleParam.leftMargin = 100;
        ruleParam.topMargin = 50;
        tvRule.setLayoutParams(ruleParam);
        linearLayout.addView(tvRule);
        rightLayout.addView(linearLayout);

    }

    /**
     * 设置个人资料的布局
     */
    private void setPersonAttr() {
        LinearLayout linearLayout1  = new LinearLayout(getApplicationContext());
        //width,height
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setLayoutParams(params);
        //子TextView的属性
        LinearLayout.LayoutParams tvParam = new LinearLayout.LayoutParams(350,LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParam.leftMargin = 30;
        tvParam.topMargin = 30;
        TextView tvPhoto = new TextView(getApplicationContext());
        tvPhoto.setText("头像:");
        tvPhoto.setTextSize(20);
        tvPhoto.setLayoutParams(tvParam);
        linearLayout1.addView(tvPhoto);
        //头像
        ivHeader = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams ivParam = new LinearLayout.LayoutParams(200,200);
        ivParam.leftMargin = 20;
        ivHeader.setBackgroundResource(R.mipmap.bg_man);
        //TODO:传图片地址及设置名称
        Glide.with(this)
                .load("")
                .circleCrop()
                .into(ivHeader);
        ivHeader.setLayoutParams(ivParam);
        linearLayout1.addView(ivHeader);
        rightLayout.addView(linearLayout1);
        //昵称
        LinearLayout linearLayout2 = new LinearLayout(getApplicationContext());
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setLayoutParams(params);
        TextView tvNiName = new TextView(getApplicationContext());
        tvNiName.setText("昵称：");
        tvNiName.setTextSize(20);
        tvNiName.setLayoutParams(tvParam);
        linearLayout2.addView(tvNiName);
        EditText etNiName = new EditText(getApplicationContext());
        etNiName.setText("小美");
        etNiName.setTextSize(20);
        etNiName.setLayoutParams(tvParam);
        linearLayout2.addView(etNiName);
        rightLayout.addView(linearLayout2);
        //个性签名
        LinearLayout linearLayout3 = new LinearLayout(getApplicationContext());
        linearLayout3.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout3.setLayoutParams(params);
        TextView tvSignature = new TextView(getApplicationContext());
        tvSignature.setText("个性签名：");
        tvSignature.setTextSize(20);
        tvSignature.setLayoutParams(tvParam);
        linearLayout3.addView(tvSignature);
        EditText etSignature = new EditText(getApplicationContext());

        etSignature.setText("第五美女");
        etSignature.setTextSize(20);
        etSignature.setLayoutParams(tvParam);
        linearLayout3.addView(etSignature);
        rightLayout.addView(linearLayout3);
        //性别
        LinearLayout linearLayout4 = new LinearLayout(getApplicationContext());
        linearLayout4.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout4.setLayoutParams(params);
        TextView tvSex = new TextView(getApplicationContext());
        tvSex.setText("性别：");
        tvSex.setTextSize(20);
        tvSex.setLayoutParams(tvParam);
        linearLayout4.addView(tvSex);
        EditText etSex = new EditText(getApplicationContext());
        etSex.setText("女");
        etSex.setTextSize(20);
        etSex.setLayoutParams(tvParam);
        linearLayout4.addView(etSex);
        rightLayout.addView(linearLayout4);
        //手机号
        LinearLayout linearLayout5 = new LinearLayout(getApplicationContext());
        linearLayout5.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout5.setLayoutParams(params);
        TextView tvPhone = new TextView(getApplicationContext());
        tvPhone.setText("手机号：");
        tvPhone.setTextSize(20);
        tvPhone.setLayoutParams(tvParam);
        linearLayout5.addView(tvPhone);
        EditText etPhone = new EditText(getApplicationContext());
        etPhone.setText("123456");
        etPhone.setTextSize(20);
        etPhone.setLayoutParams(tvParam);
        linearLayout5.addView(etPhone);
        rightLayout.addView(linearLayout5);

        //提交
        Button btnSub = new Button(getApplicationContext());
        btnSub.setText("保存");
        btnSub.setTextSize(20);
        btnSub.setBackgroundResource(R.color.ourDynastyRed);
        LinearLayout.LayoutParams btnParam = new LinearLayout.LayoutParams(280, 130);
        btnParam.gravity = Gravity.RIGHT;
        btnParam.topMargin = 20;
        btnParam.rightMargin = 20;
        btnSub.setLayoutParams(btnParam);
        rightLayout.addView(btnSub);

        //修改图片
        ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,1);
            }
        });

        //TODO：提交按钮的监听器
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String niName = etNiName.getText().toString().trim();
                String signature = etSignature.getText().toString().trim();
                String sex = etSex.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                Log.e("提交",niName+signature+sex+phone);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data){
            picturePath = loadImagePath(data);
            ivHeader.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    private String loadImagePath(Intent data) {
        //获取返回的数据，这里是android自定义的uri地址
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Log.e("filePathColumn的值",filePathColumn[0]);
        //获取选择照片的数据视图
        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
        cursor.moveToFirst();
        //从数据视图中获取已选择图片的路径
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        Log.e("columnIndex的值",columnIndex+"");
        String path = cursor.getString(columnIndex);
        Log.e("path的值",path);
        cursor.close();
        return path;
    }
}
