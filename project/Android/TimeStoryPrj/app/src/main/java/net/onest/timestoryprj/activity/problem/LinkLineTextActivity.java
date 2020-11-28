package net.onest.timestoryprj.activity.problem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.LinkDataBean;
import net.onest.timestoryprj.view.LinkLineView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinkLineTextActivity extends AppCompatActivity {

    @BindView(R.id.link_line_view)
    LinkLineView linkLineView;
    @BindView(R.id.fl_link_line)
    FrameLayout flLinkLine;
    @BindView(R.id.tv_result)
    TextView tvResult;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, LinkLineTextActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_link_line);
        ButterKnife.bind(this);

        List<LinkDataBean> list = new ArrayList<>();
        for(int i =0;i<4;i++){
            LinkDataBean linkDataBean = new LinkDataBean();
            if(i==0)
            linkDataBean.setContent("王伟");
            if(i==1)
                linkDataBean.setContent("李玮玮");
            if(i==2)
                linkDataBean.setContent("王伟伟");
            if(i==3)
                linkDataBean.setContent("刘冠军");
            linkDataBean.setQ_num(i);
            linkDataBean.setRow(i);
            linkDataBean.setCol(0);
            list.add(linkDataBean);

        }
        for(int i =0;i<4;i++){
            LinkDataBean linkDataBean = new LinkDataBean();
            if(i==0)
                linkDataBean.setContent("android");
            if(i==1)
                linkDataBean.setContent("java");
            if(i==2)
                linkDataBean.setContent("h5");
            if(i==3)
                linkDataBean.setContent("aiii");
            linkDataBean.setRow(i);
            linkDataBean.setQ_num(i);
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
    }
}
