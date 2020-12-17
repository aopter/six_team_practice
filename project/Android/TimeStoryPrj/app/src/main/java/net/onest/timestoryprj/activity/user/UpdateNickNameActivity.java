package net.onest.timestoryprj.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.util.ToastUtil;

public class UpdateNickNameActivity extends AppCompatActivity {
    private ImageView ivBack;
    private Button btnSave;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick_name);

        ivBack = findViewById(R.id.iv_back);
        btnSave = findViewById(R.id.btn_nickname_save);
        etName = findViewById(R.id.et_nickname);

        etName.setText(Constant.UserDetails.getUserNickname());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = etName.getText().toString().trim();
                Constant.User.setUserNickname(nickname);
                Constant.UserDetails.setUserNickname(nickname);
                ToastUtil.showEncourageToast(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT);
                finish();
            }
        });
    }
}
