package net.onest.timestoryprj.activity.problem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import net.onest.timestoryprj.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectProblemTypeActivity extends AppCompatActivity {

    //连线
    @BindView(R.id.type_problem_lian)
    LinearLayout llProblemLian;

    //选择
    @BindView(R.id.type_problem_xuan)
    LinearLayout llProblemXuan;
    //排序
    @BindView(R.id.type_problem_pai)
    LinearLayout llProblemPai;
    //快速
    @BindView(R.id.type_problem_all)
    LinearLayout llProblemAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_problem_type);
        ButterKnife.bind(this);
    }

//   选择题目种类跳转
    @OnClick(R.id.type_problem_lian)
    public void llProblemLianOnClick(){
//        跳转
        Intent intent = new Intent();
        intent.putExtra("type","lian");
        intent.setClass(SelectProblemTypeActivity.this, ProblemInfoActivity.class);
        startActivity(intent);

    }
    @OnClick(R.id.type_problem_xuan)
    public void llProblemXuanOnClick(){
//        选择题
        Intent intent = new Intent();
        intent.putExtra("type","xuan");
        intent.setClass(SelectProblemTypeActivity.this, ProblemInfoActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.type_problem_pai)
    public void llProblemPaiOnClick(){
        //        排序题
        Intent intent = new Intent();
        intent.putExtra("type","pai");
        intent.setClass(SelectProblemTypeActivity.this, ProblemInfoActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.type_problem_all)
    public void llProblemAllOnClick(){
        //        所有题
        Intent intent = new Intent();
        intent.putExtra("type","all");
        intent.setClass(SelectProblemTypeActivity.this, ProblemInfoActivity.class);
        startActivity(intent);
    }
}
