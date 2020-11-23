package net.onest.timestoryprj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

<<<<<<< Updated upstream
import net.onest.timestoryprj.activity.dynasty.HomepageActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnHomepage;
=======
import net.onest.timestoryprj.activity.user.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< Updated upstream
        btnHomepage = findViewById(R.id.btn_homepage);
        btnHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HomepageActivity.class);
=======

        btnLogin = findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
>>>>>>> Stashed changes
                startActivity(intent);
            }
        });
    }
}
