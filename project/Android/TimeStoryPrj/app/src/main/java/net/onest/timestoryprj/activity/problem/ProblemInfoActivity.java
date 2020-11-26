package net.onest.timestoryprj.activity.problem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.LinkDataBean;
import net.onest.timestoryprj.entity.Problem;
import net.onest.timestoryprj.view.LinkLineView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProblemInfoActivity extends AppCompatActivity {


    @BindView(R.id.link_line_view)
    LinkLineView linkLineView;
    @BindView(R.id.fl_link_line)
    FrameLayout flLinkLine;
    @BindView(R.id.tv_result)
    TextView tvResult;

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
        Problem problem = (Problem) intent.getSerializableExtra("problem");
        if (null != problem)
            Log.e("onCreate: ", problem.getProblemType() + "");
        if (null != type) {
            switch (type) {
                case "xuan":
                    llTypeLian.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    break;
                case "lian":
                    llTypeXuan.setVisibility(View.INVISIBLE);
                    llTypePai.setVisibility(View.INVISIBLE);
                    List<LinkDataBean> list = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        LinkDataBean linkDataBean = new LinkDataBean();
                        if (i == 0) {
                            linkDataBean.setContent("王伟");
                            linkDataBean.setQ_num(3);
                        }
                        if (i == 1) {
                            linkDataBean.setContent("李玮玮");
                            linkDataBean.setQ_num(2);
                        }
                        if (i == 2) {
                            linkDataBean.setContent("王伟伟");
                            linkDataBean.setQ_num(1);
                        }
                        if (i == 3) {
                            linkDataBean.setContent("刘冠军");
                            linkDataBean.setQ_num(0);
                        }

                        linkDataBean.setRow(i);
                        linkDataBean.setCol(0);
                        list.add(linkDataBean);

                    }
                    for (int i = 0; i < 4; i++) {
                        LinkDataBean linkDataBean = new LinkDataBean();
                        if (i == 0) {
                            linkDataBean.setContent("android");
                            linkDataBean.setQ_num(2);
                        }
                        if (i == 1) {
                            linkDataBean.setContent("java");
                            linkDataBean.setQ_num(3);
                        }
                        if (i == 2) {
                            linkDataBean.setContent("h5");
                            linkDataBean.setQ_num(0);
                        }
                        if (i == 3) {
                            linkDataBean.setContent("aiii");
                            linkDataBean.setQ_num(1);
                        }
                        linkDataBean.setRow(i);

                        linkDataBean.setCol(1);
                        list.add(linkDataBean);
                    }


                    linkLineView.setData(list);
                    linkLineView.setOnChoiceResultListener((correct, yourAnswer) -> {
                        // 结果
                        StringBuilder sb = new StringBuilder();
                        sb.append("正确与否：");
                        sb.append(correct);
                        sb.append("\n");
                        tvResult.setText(sb.toString());
                    });

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
