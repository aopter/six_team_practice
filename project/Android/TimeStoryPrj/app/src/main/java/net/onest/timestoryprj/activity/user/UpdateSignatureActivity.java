package net.onest.timestoryprj.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;

public class UpdateSignatureActivity extends AppCompatActivity {
    private ImageView ivBack;
    private Button btnSave;
    private EditText etSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_signature);

        ivBack = findViewById(R.id.iv_back2);
        btnSave = findViewById(R.id.btn_signature_save);
        etSignature = findViewById(R.id.et_signature);
        etSignature.setText(Constant.UserDetails.getUserSignature());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String signature = etSignature.getText().toString().trim();
                Constant.User.setUserSignature(signature);
                Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
