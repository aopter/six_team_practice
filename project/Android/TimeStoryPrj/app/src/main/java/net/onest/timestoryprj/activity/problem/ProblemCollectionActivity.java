package net.onest.timestoryprj.activity.problem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.user.UserCenterActivity;
import net.onest.timestoryprj.adapter.problem.MyExpandableListAdapter;
import net.onest.timestoryprj.adapter.problem.ProblemInfoListAdapter;
import net.onest.timestoryprj.adapter.user.UserRankListAdapter;
import net.onest.timestoryprj.entity.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProblemCollectionActivity extends AppCompatActivity {

    @BindView(R.id.elv_type)
    ExpandableListView elvListView;
    @BindView(R.id.re_problems)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_collection);
        ButterKnife.bind(this);
//        初始化数据
        init();
//        初始化题目
        List<Problem> problems = new ArrayList<>();
        for(int i=0 ;i<6;i++){
            Problem problem = new Problem();
            problem.setProblemType((i%3)+1);
            problems.add(problem);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProblemCollectionActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        ProblemInfoListAdapter problemInfoListAdapter = new ProblemInfoListAdapter(this, problems);
        recyclerView.setAdapter(problemInfoListAdapter);


    }

    private void init() {
        //模拟数据（数组，集合都可以，这里使用数组）
        final String[] groups = new String[]{"题目类型", "朝代"};
        final String[][] infos =
                new String[][]{{"选择", "连线", "排序"}, {"秦", "两汉", "三国",
                        "魏", "晋", "南北朝", "南北朝", "南北朝", "南北朝", "南北朝"}};

        //创建并设置适配器
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(groups, infos, this);
        elvListView.setAdapter(adapter);

//        //默认展开第一个分组
//        elvListView.expandGroup(0);

        //展开某个分组时，并关闭其他分组。注意这里设置的是 ExpandListener
        elvListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //遍历 group 的数组（或集合），判断当前被点击的位置与哪个组索引一致，不一致就合并起来。
                for (int i = 0; i < groups.length; i++) {
                    if (i != groupPosition) {
                        elvListView.collapseGroup(i); //收起某个指定的组
                    }
                }
            }
        });

        //点击某个分组时，展开
        elvListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                Toast.makeText(ExpandableListViewActivity.this, "组被点击了，跳转到具体的Activity", Toast.LENGTH_SHORT).show();
                {
                    boolean isExpand = elvListView.isGroupExpanded(groupPosition);//判断分组是否展开
                    if (isExpand) {
                        elvListView.collapseGroup(groupPosition);
                    } else {
                        elvListView.expandGroup(groupPosition);
                    }
                }
                return true;    //拦截点击事件，不再处理展开或者收起
            }
        });

        //某个分组中的子View被点击时的事件
        elvListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
                                        long id) {
                Toast.makeText(ProblemCollectionActivity.this, "组中的条目被点击：" + groups[groupPosition] + "的" +
                        infos[groupPosition][childPosition] + "放学后到校长办公室", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }
}
