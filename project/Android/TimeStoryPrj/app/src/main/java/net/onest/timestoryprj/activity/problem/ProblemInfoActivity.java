package net.onest.timestoryprj.activity.problem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import net.onest.timestoryprj.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProblemInfoActivity extends AppCompatActivity {

    @BindView(R.id.type_xuan)
    LinearLayout llTypeXuan;
    @BindView(R.id.type_pai)
    LinearLayout llTypePai;
    @BindView(R.id.type_lian)
    LinearLayout llTypeLian;
    LinearLayout llTypeAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_info);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if(null!=type){
            switch (type){
                case "xuan":
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    break;
                case "lian":
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    break;
                case "all":
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    break;
                case "pai":
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    llTypeLian.setVisibility(View.INVISIBLE);
                    break;

            }
        }

    }
}
