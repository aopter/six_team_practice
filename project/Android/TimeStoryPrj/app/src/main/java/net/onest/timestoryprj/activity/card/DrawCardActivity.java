package net.onest.timestoryprj.activity.card;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.card.ShareAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.User;
import net.onest.timestoryprj.entity.card.Card;
import net.onest.timestoryprj.entity.card.Icon;
import net.onest.timestoryprj.util.ScreenUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//抽卡

public class DrawCardActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.card1)
    ImageView card1;
    @BindView(R.id.card2)
    ImageView card2;
    @BindView(R.id.card3)
    ImageView card3;
    @BindView(R.id.card4)
    ImageView card4;
    @BindView(R.id.to_last_view)
    Button toLastView;
    @BindView(R.id.card_container)
    LinearLayout cardContainer;
    @BindView(R.id.front_container)
    LinearLayout frontContainer;
    @BindView(R.id.draw_card_show)
    ImageView drawCard;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.share)
    ImageView btnShare;
    @BindView(R.id.icon_view)
    GridView iconView;
    @BindView(R.id.share_container)
    RelativeLayout shareContainer;
    @BindView(R.id.e_r_code)
    ImageView ERCode;
    @BindView(R.id.join)
    TextView join;
    //    @BindView(R.id.man)
//    ImageView man;
    private List<Icon> icons;
    // 是否已点击
    private boolean flag = false;

    private boolean isFlag = false;
    // 获取卡片
    private Card card;
    private Animation cardAnimation;
    private AnimatorSet animatorSet;
    private OkHttpClient client;
    private Gson gson;
    private Bitmap shareBitmap;
    // 是否在分享状态
    private boolean isShareing = false;

    /**
     * 文本类型
     */
    public static int TEXT = 0;
    /**
     * 图片类型
     */
    public static int DRAWABLE = 1;
    int width;
    int height;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    Log.e("info", result);
                    card = gson.fromJson(result, Card.class);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_card);
        ButterKnife.bind(this);
        if (null != savedInstanceState) {
//            Log.e("onsave", "初始化了");
//            // 状态1：抽卡界面（已选取卡片）
//            // 状态2：抽卡卡片已显示
//            // 状态3：分享界面
//            isFlag = savedInstanceState.getBoolean("isFlag");
//            flag = savedInstanceState.getBoolean("flag");
//            isShareing = savedInstanceState.getBoolean("isShareing");
//            card = (Card) savedInstanceState.getSerializable("card");
//            tip.setText(savedInstanceState.getString("tip"));
//            // 已点击4张卡片
//            if (isFlag) {
//                frontContainer.setAlpha(0);
//                cardContainer.setAlpha(0.8f);
//                // 如果已点击图片，获得卡片展示(默认)
//                if (flag) {
//                    drawCard.setBackground(getResources().getDrawable(R.mipmap.card_bg));
//                    Glide.with(getApplicationContext())
//                            .load(ServiceConfig.SERVICE_ROOT + "/img/" + card.getCardPicture())
//                            .into(drawCard);
//                } else {
//                    drawCard.setBackground(getResources().getDrawable(R.mipmap.card_back));
//                }
//            }
//            toLastView.setVisibility(savedInstanceState.getInt("toLastView"));
//            btnShare.setVisibility(savedInstanceState.getInt("btnShare"));
//            ERCode.setVisibility(savedInstanceState.getInt("ERCode"));
//            join.setVisibility(savedInstanceState.getInt("join"));
//            shareContainer.setVisibility(savedInstanceState.getInt("shareContainer"));
        } else {
            // TODO 记得删除
            Constant.User = new User();
            Constant.User.setUserId(1);
            back.setVisibility(View.VISIBLE);
            width = ScreenUtil.dip2px(getApplicationContext(), 120);
            height = ScreenUtil.dip2px(getApplicationContext(), 180);
            final Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/custom_font.ttf");
            tip.setTypeface(typeface);
            text.setTypeface(typeface);
            client = new OkHttpClient();
            frontContainer.bringToFront();
            cardAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.back);
            gson = new GsonBuilder()//创建GsonBuilder对象
                    .serializeNulls()//允许输出Null值属性
                    .create();//创建Gson对象
            getDrawCard();
            initShareView();
        }
    }

    private void initShareView() {
        icons = new ArrayList<>();
        Icon icon1 = new Icon();
        icon1.setIconId(R.mipmap.qq);
        icon1.setName("分享到qq");
        icons.add(icon1);
        Icon icon2 = new Icon();
        icon2.setIconId(R.mipmap.weixin);
        icon2.setName("分享到微信");
        icons.add(icon2);
        Icon icon3 = new Icon();
        icon3.setIconId(R.mipmap.frend_circle);
        icon3.setName("分享到朋友圈");
        icons.add(icon3);
        ShareAdapter shareAdapter = new ShareAdapter(getApplicationContext(), icons);
        iconView.setAdapter(shareAdapter);
        iconView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    Log.e("d", "点击了qq");
                    shareContainer.setVisibility(View.INVISIBLE);
                    ScreenShot sh = new ScreenShot();
                    sh.start();
                    try {
                        sh.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    shareQQFriend("卡片分享", "您的好友向你分享了卡片", DrawCardActivity.DRAWABLE, shareBitmap);
                } else if (position == 1) {
                    Log.e("d", "点击了wechat");
                    shareContainer.setVisibility(View.INVISIBLE);
                    ScreenShot sh = new ScreenShot();
                    sh.start();
                    try {
                        sh.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    shareWeChatFriend("卡片分享", "您的好友向你分享了卡片", DrawCardActivity.DRAWABLE, shareBitmap);
                } else if (position == 2) {
                    // TODO 分享到朋友圈
                    Log.e("d", "点击了朋友圈");
                    shareContainer.setVisibility(View.INVISIBLE);
                    ScreenShot sh = new ScreenShot();
                    sh.start();
                    try {
                        sh.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    shareWeChatFriendCircle("卡片分享", "您的好友向你分享了一张卡片", shareBitmap);
                }
            }
        });
    }

    private void getDrawCard() {
        new Thread() {
            @Override
            public void run() {
                Log.e("draw", ServiceConfig.SERVICE_ROOT + "/card/draw/" + Constant.User.getUserId());
                Request request = new Request.Builder()
                        .url(ServiceConfig.SERVICE_ROOT + "/card/draw/" + Constant.User.getUserId())
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // TODO获取卡片内容失败
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.e("log", result);
                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    @OnClick(R.id.draw_card_show)
    void showCard() {
        if (!isShareing) {
            if (!flag) {
                // TODO 恭喜
                flag = true;
                shareContainer.setVisibility(View.VISIBLE);
                toLastView.setVisibility(View.VISIBLE);
                btnShare.setVisibility(View.VISIBLE);
                tip.setText("恭喜你获得‘" + card.getCardName() + "’的卡片");
                // 调用setAnimationListener方法对动画的实现过程进行监听
                cardAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onAnimationEnd(Animation animation) {//当动画结束时需要执行的行为
                        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.front);
                        drawCard.setBackground(getResources().getDrawable(R.mipmap.card_bg));
                        Glide.with(getApplicationContext())
                                .load(ServiceConfig.SERVICE_ROOT + "/img/" + card.getCardPicture())
                                .into(drawCard);
                        drawCard.startAnimation(animation);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                drawCard.startAnimation(cardAnimation);
            } else {
                Intent intent = new Intent(getApplicationContext(), SpectficCardDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("cardId", card.getCardId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//有版本限制
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, drawCard, "ivGetCard").toBundle());
                }
                //开始下一个activity     android:transitionName="ivGetCard"
            }
        }
    }

    @OnClick({R.id.card1, R.id.card2, R.id.card3, R.id.card4})
    void showCardContainerPage() {
        if (!isFlag) {
            animatorSet = new AnimatorSet();
            ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(
                    cardContainer,
                    "alpha",
                    0, 0.8f
            );
            alphaAnim.setDuration(400);
            ObjectAnimator alphaAnim1 = ObjectAnimator.ofFloat(
                    frontContainer,
                    "alpha",
                    0.95f, 0
            );
            alphaAnim1.setDuration(100);
            animatorSet.setDuration(1000);
            animatorSet.play(alphaAnim1).before(alphaAnim);
            animatorSet.start();
            isFlag = true;
            cardContainer.bringToFront();
        }
        Log.e("card", isFlag + "");
    }


    @OnClick({R.id.back, R.id.to_last_view})
    void backToLastPage() {
        finish();
    }

    @OnClick(R.id.share)
    void toShare() {
        shareContainer.setVisibility(View.VISIBLE);
        toLastView.setVisibility(View.INVISIBLE);
        join.setVisibility(View.VISIBLE);
        ERCode.setVisibility(View.VISIBLE);
        shareContainer.bringToFront();
        isShareing = true;
    }

    @OnClick({R.id.card_container, R.id.draw_card_show})
    void showOriginPage() {
        if (isFlag) {
            shareContainer.setVisibility(View.GONE);
            toLastView.setVisibility(View.VISIBLE);
            join.setVisibility(View.INVISIBLE);
            ERCode.setVisibility(View.INVISIBLE);
            shareContainer.bringToFront();
            isShareing = false;
        }
    }

    private class ScreenShot extends Thread {
        @Override
        public void run() {
            // 获取屏幕
            View view = getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            shareBitmap = view.getDrawingCache();
        }
    }

    /**
     * 保存机制
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("onsave", "保存了");
        // 保存card信息
        outState.putSerializable("card", card);
        // 是否选取了卡片
        outState.putBoolean("isFlag", isFlag);
        // 保存状态：以实现动画/为实现动画
        outState.putBoolean("flag", flag);
        // 保存状态：分享/不分享
        outState.putBoolean("isShareing", isShareing);
        // 保存各个container的状态
        outState.putInt("toLastView", toLastView.getVisibility());
        outState.putInt("shareContainer", shareContainer.getVisibility());
        outState.putInt("btnShare", btnShare.getVisibility());
        outState.putInt("ERCode", ERCode.getVisibility());
        outState.putInt("join", join.getVisibility());
        String text = tip.getText().toString();
        outState.putString("tip", text);
    }

    /**
     * 分享到QQ好友或群组
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param type     (分享类型)
     * @param drawable (分享图片，若分享类型为AndroidShare.TEXT，则可以为null)
     */
    public void shareQQFriend(String msgTitle, String msgText, int type, Bitmap drawable) {
        shareMsg("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity", "QQ", msgTitle,
                msgText, type, drawable);
    }

    /**
     * 分享到微信好友
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param type     (分享类型)
     * @param drawable (分享图片，若分享类型为AndroidShare.TEXT，则可以为null)
     */
    public void shareWeChatFriend(String msgTitle, String msgText, int type, Bitmap drawable) {
        shareMsg("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI", "微信",
                msgTitle, msgText, type, drawable);
    }

    /**
     * 分享到微信朋友圈(分享朋友圈一定需要图片)
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param drawable (分享图片)
     */
    public void shareWeChatFriendCircle(String msgTitle, String msgText,
                                        Bitmap drawable) {
        shareMsg("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI",
                "微信", msgTitle, msgText, DrawCardActivity.DRAWABLE, drawable);
    }

    /**
     * 点击分享的代码
     *
     * @param packageName  (包名,跳转的应用的包名)
     * @param activityName (类名,跳转的页面名称)
     * @param appname      (应用名,跳转到的应用名称)
     * @param msgTitle     (标题)
     * @param msgText      (内容)
     * @param type         (发送类型：text or pic 微信朋友圈只支持pic)
     */
    @SuppressLint("NewApi")
    private void shareMsg(String packageName, String activityName,
                          String appname, String msgTitle, String msgText, int type,
                          Bitmap drawable) {
        if (!packageName.isEmpty() && !isAvilible(getApplicationContext(), packageName)) {// 判断APP是否存在
            Toast.makeText(getApplicationContext(), "请先安装" + appname, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        if (type == DrawCardActivity.TEXT) {
            intent.setType("text/plain");
        } else if (type == DrawCardActivity.DRAWABLE) {
            intent.setType("image/*");
            final Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), drawable, "IMG" + Calendar.getInstance().getTime(), null));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!packageName.isEmpty()) {
            intent.setComponent(new ComponentName(packageName, activityName));
            getApplication().startActivity(intent);
        } else {
            getApplication().startActivity(Intent.createChooser(intent, msgTitle));
        }
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 指定分享到qq
     *
     * @param context
     * @param bitmap
     */
    public void sharedQQ(Activity context, Bitmap bitmap) {
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
                context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher), null, null));
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setPackage("com.tencent.mobileqq");
        imageIntent.setType("image/*");
        imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        imageIntent.putExtra(Intent.EXTRA_TEXT, "您的好友向你展示了卡片");
        imageIntent.putExtra(Intent.EXTRA_TITLE, "时光序");
        context.startActivity(imageIntent);
    }
}
