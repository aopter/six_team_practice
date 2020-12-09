package net.onest.timestoryprj.activity.problem;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tinytongtong.tinyutils.LogUtils;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.problem.MyExpandableListAdapter;
import net.onest.timestoryprj.adapter.problem.ProblemInfoListAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.Problem;
import net.onest.timestoryprj.entity.problem.ProblemCollection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    //    刷新
    @BindView(R.id.refreshLayout_collction)
    SmartRefreshLayout refreshLayout;

    private List<ProblemCollection> problemCollections;//题目列表
    private List<Problem> problems;//题目列表
    private OkHttpClient okHttpClient;
    private int cPageCount;//当前页
    private Gson gson;
    private String pageCount = "1";//分页数
    private int pageSize = 3;//容量
    private String cDynastyId = "0";//当前朝代
    private int cType = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.arg1) {
                case 1://题目下载好
//                    初始化adapter
                    if (cPageCount == 1) {//第一页
                        initAdapter();
                        refreshLayout.finishRefresh();
                    } else {//其余是更新的
//
                        ProblemCollectionActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                                adapter.notifyItemRangeInserted(problems.size() - msg.arg2, msg.arg2);
                                refreshLayout.finishLoadMore();//加载完毕
                            }
                        });
                    }
                    break;

            }
        }
    };

//    private void initAdapter() {//初始化adapter
//        ProblemCollectionActivity.this.runOnUiThread(new Runnable() {
//            public void run() {
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProblemCollectionActivity.this);
//                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                recyclerView.setLayoutManager(linearLayoutManager);
//                ProblemInfoListAdapter problemInfoListAdapter = new ProblemInfoListAdapter(getApplicationContext(), problems);
//                recyclerView.setAdapter(problemInfoListAdapter);
//            }
//        });
//    }

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
        init();//所有朝代
        initLeftMenu();//初始化左边

        refreshLayout.setReboundDuration(300);//回弹动画时常
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新
                cPageCount = 1;
                init();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d("当前最大页数",pageCount+"");
                LogUtils.d("当前页数",cPageCount+"");
                //加载更多
                //获得当前页 获得最大页 ++
                if (cPageCount < Integer.parseInt(pageCount)) {
                    //加载
                    cPageCount += 1;
                    Log.e("onLoadMore: ", cPageCount + "");
                    //请求 通知数据更新
                    loadMore();

                } else {//没有更多数据
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
        });
    }

    private void loadMore() {//加载更多

    }



    private void initAdapter() {//初始化adapter
        ProblemCollectionActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProblemCollectionActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                ProblemInfoListAdapter problemInfoListAdapter = new ProblemInfoListAdapter(getApplicationContext(), problems);
                recyclerView.setAdapter(problemInfoListAdapter);
            }
        });

        String pUrl = ServiceConfig.SERVICE_ROOT + "/userproblem/search/" + Constant.User.getUserId() + "/" + cPageCount + "/" + pageSize + "";
        if (!cDynastyId.equals("0")) {//按朝代分
            pUrl = ServiceConfig.SERVICE_ROOT + "/userproblem/search/" + Constant.User.getUserId() + "/" + cDynastyId + "/" + cPageCount + "/" + pageSize + "";
            Log.e("run:pUrl ", pUrl);
        }
        if (cType != 0) {
            pUrl = ServiceConfig.SERVICE_ROOT + "/userproblem/searchlist/" + Constant.User.getUserId() + "/" + cType + "/" + cPageCount + "/" + pageSize + "";
        }
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
                List<ProblemCollection> pcs1 = new ArrayList<>();
                pcs1 = gson.fromJson(problemJson, new TypeToken<List<ProblemCollection>>() {
                }.getType());
                LogUtils.d("pcs1", pcs1.size() + "");
                for (int i = 0; i < pcs1.size(); i++) {
                    problemCollections.add(pcs1.get(i));
                    problemCollections.get(i).getProblem().setDynastyId(problemCollections.get(i).getDynasty().getDynastyId() + "");
                    problems.add(pcs1.get(i).getProblem());
                }
                LogUtils.d(problems.size() + "数量");
                Message message = new Message();
                message.arg1 = 1;
                message.arg2 = pcs1.size();//数量
                message.obj = problemJson;
                handler.handleMessage(message);
            }
        });
    }


    private void init() {//获得用户收藏的题目

        cPageCount = 1;
        String url = ServiceConfig.SERVICE_ROOT + "/userproblem/count/" + Constant.User.getUserId() + "/" + pageSize + "";

        if (!cDynastyId.equals("0")) {
            url = ServiceConfig.SERVICE_ROOT + "/userproblem/count/" + Constant.User.getUserId() + "/" + cDynastyId + "/" + pageSize + "";
        }
        if (cType != 0) {
            url = ServiceConfig.SERVICE_ROOT + "/userproblem/typecount/" + Constant.User.getUserId() + "/" + cType + "/" + pageSize + "";
        }

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
                    String data = execute.body().string();
                    Log.e("init分页数: ", data);
                    JSONObject count = new JSONObject(data);
                    pageCount = count.getString("count");//分页数
//                   获取题目
//        用户分页查询自己全部收藏的题目：
                    String pUrl = ServiceConfig.SERVICE_ROOT + "/userproblem/search/" + Constant.User.getUserId() + "/" + cPageCount + "/" + pageSize + "";
                    if (!cDynastyId.equals("0")) {//按朝代分
                        pUrl = ServiceConfig.SERVICE_ROOT + "/userproblem/search/" + Constant.User.getUserId() + "/" + cDynastyId + "/" + cPageCount + "/" + pageSize + "";
                    }
                    if (cType != 0) {
                        pUrl = ServiceConfig.SERVICE_ROOT + "/userproblem/searchlist/" + Constant.User.getUserId() + "/" + cType + "/" + cPageCount + "/" + pageSize;
                    }

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

                            problemCollections.clear();//清空
                            problems.clear();

                            problemCollections = gson.fromJson(problemJson, new TypeToken<List<ProblemCollection>>() {
                            }.getType());
                            Log.e("problemCollections数目 ", problemCollections.size() + "");
                            for (int i = 0; i < problemCollections.size(); ++i) {
                                //设置朝代号
                                problemCollections.get(i).getProblem().setDynastyId(problemCollections.get(i).getDynasty().getDynastyId() + "");
                                problems.add(problemCollections.get(i).getProblem());

                            }
                            Message message = new Message();
                            message.arg1 = 1;
                            message.obj = problemJson;
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
    }

    private void initLeftMenu() {


//模拟数据（数组，集合都可以，这里使用数组）
        final String[] groups = new String[]{"题目类型", "朝代"};

        String[] dynastyName = new String[Constant.UnlockDynasty.size()];
        Log.e("init: ", dynastyName.length + "");
        for (int i = dynastyName.length-1; i > 0; i--) {
            dynastyName[i] = Constant.UnlockDynasty.get(i).getDynastyName();
        }
        final String[][] infos =
                new String[][]{{"选择", "连线", "排序"}, dynastyName};

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
//                Toast.makeText(ProblemCollectionActivity.this, "组中的条目被点击：" + groups[groupPosition] + "的" +
//                        infos[groupPosition][childPosition] + "放学后到校长办公室", Toast.LENGTH_SHORT).show();
                cPageCount = 1;
//                解锁的朝代
                switch (groupPosition) {
                    case 0://类型
                        cType = childPosition + 1;
                        cDynastyId = "0";
                        init();

                        break;
                    case 1://朝代
                        cType = 0;
                        cDynastyId = Constant.UnlockDynasty.get(childPosition).getDynastyId();
                        Log.e("onChildClick:dynastyId ", cDynastyId);
                        init();//加载
                        break;
                }
//                点击
                return false;
            }
        });

    }
}
