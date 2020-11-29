package net.onest.timestoryprj.activity.problem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.user.UserCenterActivity;
import net.onest.timestoryprj.adapter.problem.MyExpandableListAdapter;
import net.onest.timestoryprj.adapter.problem.ProblemInfoListAdapter;
import net.onest.timestoryprj.adapter.user.UserRankListAdapter;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Problem;
import net.onest.timestoryprj.entity.User;
import net.onest.timestoryprj.entity.problem.ProblemCollection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProblemCollectionActivity extends AppCompatActivity {

    @BindView(R.id.elv_type)
    ExpandableListView elvListView;
    @BindView(R.id.re_problems)
    RecyclerView recyclerView;

    private List<ProblemCollection> problemCollections;//题目列表
    private List<Problem> problems;//题目列表
    private OkHttpClient okHttpClient;
    private int cPageCount;
    private Gson gson;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.arg1) {
                case 1://题目下载好
//                    初始化adapter
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProblemCollectionActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    ProblemInfoListAdapter problemInfoListAdapter = new ProblemInfoListAdapter(getApplicationContext(), problems);
                    recyclerView.setAdapter(problemInfoListAdapter);

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_collection);
        ButterKnife.bind(this);
        okHttpClient = new OkHttpClient();
        problems = new ArrayList<>();
        problemCollections = new ArrayList<>();
        cPageCount = 1;
        gson = new Gson();

//        初始化数据
        init();
//        初始化题目
//        List<Problem> problems = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            Problem problem = new Problem();
//            problem.setProblemType((i % 3) + 1);
//            problems.add(problem);
//        }




    }

    private void init() {//获得用户收藏的题目

//      用户分页查询自己收藏的某个朝代的题目：
///userproblem/search/{userId}/{dynastyId}/{pageNum}/{pageSize}
//        用户获取自己收藏题目可分的最大页数：
///userproblem/count/{userId}/{dynastyId}/{pageSize}
//        用户获取自己收藏的某个朝代题目可分的最大的页数：
///userproblem/count/{userId}/{pageSize}

//        okHttpClient
        String url = ServiceConfig.SERVICE_ROOT + "/userproblem/count/1/11/3";
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        //构造请求类
        Request request = builder.build();
        final Call call = okHttpClient.newCall(request);
        new Thread() {
            @Override
            public void run() {
                try {
                    Response execute = call.execute();
                    String pageCount = execute.body().string();
                    Log.e("init分页数: ", pageCount);
                    JSONObject count = new JSONObject(pageCount);
                    count.getString("count");//分页数
//                   获取题目
//        用户分页查询自己全部收藏的题目：
///     userproblem/search/{userId}/{pageNum}/{pageSize}
                    //下拉刷新
                    String pUrl = ServiceConfig.SERVICE_ROOT + "/userproblem/search/1/" + cPageCount + "/3";
                    Request.Builder builder = new Request.Builder();
                    builder.url(pUrl);
                    //构造请求类
                    Request request = builder.build();
                    final Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("onFailure: ", "获取失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String problemJson = response.body().string();
                            problemCollections = gson.fromJson(problemJson, new TypeToken<List<ProblemCollection>>() {
                            }.getType());

                            for(int i=0;i<problemCollections.size();++i){
                                //
                                problems.add(problemCollections.get(i).getProblem());

                            }
                            Message message = new Message();
                            message.arg1 = 1;
                            handler.handleMessage(message);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();


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
