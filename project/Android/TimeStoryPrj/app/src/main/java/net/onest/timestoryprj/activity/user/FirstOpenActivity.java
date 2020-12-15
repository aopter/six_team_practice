package net.onest.timestoryprj.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.dynasty.HomepageActivity;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.User;

import java.io.IOException;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FirstOpenActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private boolean isLogin;
    String phone;
    String pwd;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private PromptDialog promptDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {            //实现页面的跳转
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    promptDialog = (PromptDialog) msg.obj;
                    promptDialog.showError("登录失败");
                    break;
                default:
                    if (isLogin) {
                        phone = sharedPreferences.getString("phone", "");
                        pwd = sharedPreferences.getString("password", "");
//            直接登录
                        upToServer();
                    } else {
                        //跳转登录
                        Intent intent = new Intent(FirstOpenActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
            }

            super.handleMessage(msg);
        }
    };

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
                    if (result.contains("false")) {
                        //登录失败
                        Message message = handler.obtainMessage();
                        message.what = 2;
                        message.obj = promptDialog;
                        handler.sendMessage(message);
                    } else {
                        //登录成功
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        message.obj = promptDialog;
                        handler.sendMessage(message);
                        Constant.User = gson.fromJson(result, User.class);
                        Constant.User.setUserAccount(phone);

                        //跳转到主页
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), HomepageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        FirstOpenActivity.this.finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        停留两秒 自动登录
//设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first_open);
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("remember", false);
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        promptDialog = new PromptDialog(this);
        handler.sendEmptyMessageDelayed(0, 2500);
    }
}
