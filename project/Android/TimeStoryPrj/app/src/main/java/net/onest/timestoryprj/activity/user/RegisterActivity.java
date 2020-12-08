package net.onest.timestoryprj.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.ServiceConfig;

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
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        okHttpClient = new OkHttpClient();
        //注册按钮的监听
        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = etPhone.getText().toString().trim();
                pwd = etPwd.getText().toString().trim();
                if (phone != null && pwd != null && phone.length() == 11){
                    upInfoToServer();
                }else {
                    Toast.makeText(getApplicationContext(),"请检查您的信息格式",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 将用户注册信息上传至服务器进行判断
     */
    private void upInfoToServer() {
        new Thread(){
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
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("result") == true){
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        //跳转至登录界面
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }else {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(),"注册失败，该用户已存在",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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
