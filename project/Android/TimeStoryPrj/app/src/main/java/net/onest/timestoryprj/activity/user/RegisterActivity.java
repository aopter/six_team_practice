package net.onest.timestoryprj.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.util.PhoneUtil;
import net.onest.timestoryprj.util.ToastUtil;

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

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etPhone;
    private EditText etPwd;
    private Button btnRes;
    private String phone;
    private String pwd;
    private PromptDialog promptDialog;
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result") == true) {
                            promptDialog.dismissImmediately();
                            Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                            //跳转至登录界面
                            Intent intent = new Intent();
                            intent.setClass(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            promptDialog.dismissImmediately();
                            Toast.makeText(getApplicationContext(), "注册失败，该用户已存在", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        promptDialog = new PromptDialog(this);
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(false).round(3).loadingDuration(3000);
        findViews();
        okHttpClient = new OkHttpClient();
        //注册按钮的监听
        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = etPhone.getText().toString().trim();
                pwd = etPwd.getText().toString().trim();
                if (phone != null && pwd != null && phone.length() == 11 && !"".equals(phone.trim()) && !"".equals(pwd.trim()) && pwd.trim().length() < 18 && pwd.trim().length() > 12) {
                    if (PhoneUtil.isMobileNo(phone)) {
                        upInfoToServer();
                        promptDialog.showLoading("正在注册");
                    } else {
                        ToastUtil.showSickToast(getApplicationContext(), "请输入正确的手机号码", 1500);
                    }
                } else {
                    ToastUtil.showSickToast(getApplicationContext(), "您的格式不正确，请重新检查", 1500);
                }
            }
        });
    }

    /**
     * 将用户注册信息上传至服务器进行判断
     */
    private void upInfoToServer() {
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
                            .url(ServiceConfig.SERVICE_ROOT + "/user/register")
                            .method("POST", formBody)
                            .post(formBody)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    Response response = call.execute();
                    String result = response.body().string();
                    Log.e("result", result);
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void findViews() {
        etPhone = findViewById(R.id.et_res_phone);
        etPwd = findViewById(R.id.et_res_pwd);
        btnRes = findViewById(R.id.btn_res);
    }

}
