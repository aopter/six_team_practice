package net.onest.timestoryprj.activity.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.dynasty.HomepageActivity;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.Callable;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etPhone;
    private EditText etPwd;
    private Button btnLogin;//登录
    private Button btnWeixin;//微信登录
    private Button btnQQ;//qq登录
    private Button btnRegister;//注册
    private Button btnForget;//忘记密码
    private CheckBox chRemember;
    private String phone;
    private String pwd;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private PromptDialog promptDialog;
    private Tencent mTencent;
    private BaseUiListener baseUiListener;
    private UserInfo userInfo;
    private OkHttpClient okHttpClient;
    private String openId;
    private String userSex;
    private String userNickName;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    promptDialog = (PromptDialog) msg.obj;
                    promptDialog.showSuccess("登录成功");
                    break;
                case 2:
                    promptDialog = (PromptDialog) msg.obj;
                    promptDialog.showError("登录失败");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTencent = Tencent.createInstance(1111252578+"", LoginActivity.this.getApplicationContext());

        okHttpClient = new OkHttpClient();
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);

        findViews();
        gson = new Gson();
        AssetManager assets = getAssets();
        final Typeface typeface = Typeface.createFromAsset(assets, "fonts/custom_font.ttf");
        btnLogin.setTypeface(typeface);
        btnRegister.setTypeface(typeface);
        btnForget.setTypeface(typeface);
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        boolean flag = sharedPreferences.getBoolean("remember", false);
        if (flag) {
            phone = sharedPreferences.getString("phone", "");
            pwd = sharedPreferences.getString("password", "");
            etPhone.setText(phone);
            etPwd.setText(pwd);
            chRemember.setChecked(true);
        }
        setListener();

    }

    private void setListener() {
        MyListener myListener = new MyListener();
        btnLogin.setOnClickListener(myListener);
        btnRegister.setOnClickListener(myListener);
        btnQQ.setOnClickListener(myListener);
    }

    /**
     * 获取控件对象
     */
    private void findViews() {
        etPhone = findViewById(R.id.et_phone);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnWeixin = findViewById(R.id.btn_weixin);
        btnQQ = findViewById(R.id.btn_qq);
        btnForget = findViewById(R.id.btn_forget);
        chRemember = findViewById(R.id.remember);
    }

    /**
     * 将参数上传至服务器并获取返回消息
     */
    private void upToServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    FormBody formBody = formBuilder.add("userAccount", phone)
                            .add("userPassword", pwd)
                            .add("flag", 1 + "")
                            .build();
                    Request request = new Request.Builder()
                            .url(ServiceConfig.SERVICE_ROOT + "/user/login")
                            .method("POST", formBody)
                            .post(formBody)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    Response response = call.execute();
                    String result = response.body().string();
                    Log.e("result", result);
                    if (result.contains("false")){
                        //登录失败
                        Message message = handler.obtainMessage();
                        message.what = 2;
                        message.obj = promptDialog;
                        handler.sendMessage(message);
                    }else {
                        //登录成功
                        boolean isSave = chRemember.isChecked();
                        if (isSave) {
                            saveUser();
                        } else {
                            deleteUser();
                        }
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        message.obj = promptDialog;
                        handler.sendMessage(message);
                        Constant.User = gson.fromJson(result, User.class);
                        Log.e("user信息",Constant.User.getUserCount()+"");

                        //跳转到主页
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), HomepageActivity.class);
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }.start();
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_login://登录
                    phone = etPhone.getText().toString().trim();
                    pwd = etPwd.getText().toString().trim();
                    if (null != phone && null != pwd && phone.length() == 11) {
                        upToServer();
                        promptDialog.showLoading("正在登录");
                    } else {
                        Toast.makeText(getApplicationContext(), "您的格式不正确，请重新检查", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_register://注册
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_qq://QQ登录
                    baseUiListener = new BaseUiListener();
                    Log.e("ok","正在监听");
                    mTencent.login(LoginActivity.this,"all",baseUiListener);
                    break;
            }
        }
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            Toast.makeText(getApplicationContext(),"授权成功",Toast.LENGTH_SHORT).show();
            Log.e("LoginActivity",o.toString());
            String info = o.toString();
            try {
                JSONObject object = new JSONObject(info);
                openId = object.getString("openid");
                String accessToken = object.getString("access_token");
                String expires = object.getString("expires_in");
                mTencent.setOpenId(openId);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                userInfo = new UserInfo(getApplicationContext(),qqToken);
                //上传服务器

                userInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Log.e("信息",o.toString());
                        try {
                            JSONObject obj = new JSONObject(o.toString());
                            userNickName = obj.getString("nickname");
                            userSex = obj.getString("gender");
                            String figureurl = obj.getString("figureurl_2");
                            Log.e("昵称",userNickName);
                            upInfoToServer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onWarning(int i) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onError(UiError uiError) {
            Toast.makeText(getApplicationContext(),"授权失败",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(),"授权取消",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onWarning(int i) {

        }
    }

    /**
     * QQ授权登录
     */
    private void upInfoToServer() {
        new Thread(){
            @Override
            public void run() {
                FormBody.Builder formBuilder = new FormBody.Builder();
                FormBody formBody = formBuilder.add("userAccount", openId)
                        .add("userSex",userSex)
                        .add("userNickName",userNickName)
                        .add("flag", 2 + "")
                        .build();
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/user/register")
                        .method("POST", formBody)
                        .post(formBody)
                        .build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    Log.e("result", result);
                    if (result.contains("false")){
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(),HomepageActivity.class);
                        startActivity(intent);
                        Constant.User = gson.fromJson(result,User.class);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN){
            Log.e("ok","回调函数");
            Tencent.onActivityResultData(requestCode,resultCode,data,baseUiListener);
            Log.e("iii","跳过去");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 删除用户信息
     */
    private void deleteUser() {
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        Log.e("ok", "删除成功");
        editor.commit();
    }

    /**
     * 保存用户信息
     */
    private void saveUser() {
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);
        editor.putString("password", pwd);
        editor.putBoolean("remember", true);
        Log.e("ok", "保存成功");
        editor.commit();
    }

}
