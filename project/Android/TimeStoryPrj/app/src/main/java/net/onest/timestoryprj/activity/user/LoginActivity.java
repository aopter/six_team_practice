package net.onest.timestoryprj.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
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
    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
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
                    URL url = new URL("");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    OutputStream outputStream = connection.getOutputStream();
                    JSONObject obj = new JSONObject();
                    obj.put("number", phone);
                    obj.put("password", pwd);
                    String str = obj.toString();
                    outputStream.write(str.getBytes());

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String info = reader.readLine();
                    Log.e("info", info);
                    if (info.contains("false")) {
                        //登录失败
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "登录失败，请检查您的用户名及密码", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Log.e("ok", "可以跳转");
                        //判断是否记住密码
                        boolean isSave = chRemember.isChecked();
                        if (isSave) {
                            saveUser();
                        } else {
                            deleteUser();
                        }
                        Constant.User = gson.fromJson(info, User.class);
                        //跳转到主页
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

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_login://登录
                    phone = etPhone.getText().toString().trim();
                    pwd = etPwd.getText().toString().trim();
                    if (null != phone && null != pwd && phone.length() == 11) {
                        upToServer();
                    } else {
                        Toast.makeText(getApplicationContext(), "您的格式不正确，请重新检查", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_register://注册
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                    break;
            }
        }
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
