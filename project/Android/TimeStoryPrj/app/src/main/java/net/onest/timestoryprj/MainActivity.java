package net.onest.timestoryprj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import net.onest.timestoryprj.activity.dynasty.HomepageActivity;
<<<<<<< HEAD
import net.onest.timestoryprj.activity.user.UserCenterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
=======
import net.onest.timestoryprj.activity.user.LoginActivity;
>>>>>>> 873e25da223ce291776ce426a68c361ffa082b3b

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnHomepage;

<<<<<<< HEAD
    @BindView(R.id.btn_history)
    public Button btnHis;
=======

>>>>>>> 873e25da223ce291776ce426a68c361ffa082b3b
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        ButterKnife.bind(this);
=======
>>>>>>> 873e25da223ce291776ce426a68c361ffa082b3b

        btnHomepage = findViewById(R.id.btn_homepage);
        btnLogin = findViewById(R.id.login);
        btnHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });

<<<<<<< HEAD
        btnHis.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, UserCenterActivity.class);
            Log.e("jumpHis: ","执行" );
            startActivity(intent);
=======
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
>>>>>>> 873e25da223ce291776ce426a68c361ffa082b3b
        });
    }
}
