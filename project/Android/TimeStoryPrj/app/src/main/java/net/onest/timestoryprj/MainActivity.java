package net.onest.timestoryprj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import net.onest.timestoryprj.activity.dynasty.HomepageActivity;
import net.onest.timestoryprj.activity.user.UserCenterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private Button btnHomepage;

    @BindView(R.id.btn_history)
    public Button btnHis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnHomepage = findViewById(R.id.btn_homepage);
        btnHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });

        btnHis.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, UserCenterActivity.class);
            Log.e("jumpHis: ","执行" );
            startActivity(intent);
        });
    }
}
