package net.onest.timestoryprj.activity.card;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.adapter.card.ShareAdapter;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.entity.card.Icon;
import net.onest.timestoryprj.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareCardActivity extends AppCompatActivity {
    private List<Icon> icons;
    @BindView(R.id.icon_view)
    GridView iconView;
    @BindView(R.id.share_img)
    ImageView shareImg;
    @BindView(R.id.back)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_card);
        ButterKnife.bind(this);
        shareImg.setImageBitmap(Constant.shareBitmap);
        initShareView();
    }

    @OnClick(R.id.back)
    void toLastPage() {
        finish();
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
                    shareQQFriend("卡片分享", "您的好友向你分享了卡片", DrawCardActivity.DRAWABLE, Constant.shareBitmap);
                } else if (position == 1) {
                    shareWeChatFriend("卡片分享", "您的好友向你分享了卡片", DrawCardActivity.DRAWABLE, Constant.shareBitmap);
                } else if (position == 2) {
                    shareWeChatFriendCircle("卡片分享", "您的好友向你分享了一张卡片", Constant.shareBitmap);
                }
            }
        });
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
            ToastUtil.showSickToast(getApplicationContext(), "还没有安装" + appname + "哦", 1500);
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
}
