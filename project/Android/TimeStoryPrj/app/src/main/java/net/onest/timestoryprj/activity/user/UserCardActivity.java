package net.onest.timestoryprj.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.util.ZXingUtils;

import java.util.concurrent.ExecutionException;

public class UserCardActivity extends AppCompatActivity {
    private ImageView ivImg;
    private ImageView ivCard;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    ivCard.setImageBitmap(bitmap);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card);

        ivImg = findViewById(R.id.iv_back3);
        ivCard = findViewById(R.id.iv_card);

        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new Thread(){
            @Override
            public void run() {
                try {
                    Bitmap bitmapHeader;//头像
                    if (Constant.User.getFlag()==0){
                        //手机号登录
                        if (Constant.User.getUserHeader() == null){
                            bitmapHeader = BitmapFactory.decodeResource(getResources(),R.mipmap.man);
                        }else {
                            Drawable header = Glide.with(getApplicationContext())
                                    .load(ServiceConfig.SERVICE_ROOT+"/img/"+ Constant.User.getUserHeader())
                                    .into(200,200)
                                    .get();
                            bitmapHeader = getBitmap(header);
                        }
                    }else {
                        Drawable qqHeader = Glide.with(getApplicationContext())
                                .load(Constant.User.getUserHeader())
                                .into(200,200)
                                .get();
                        bitmapHeader = getBitmap(qqHeader);
                    }

                    //二维码
                    Bitmap bitmap = ZXingUtils.createQRImage(Constant.User.getUserNickname(),500,500);
                    Bitmap logoBitmap = ZXingUtils.addLogo(bitmap,bitmapHeader,0.2F);
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = logoBitmap;
                    handler.sendMessage(message);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static Bitmap getBitmap(Drawable drawable){
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        //PixelFormat.OPAQUE:透明度
        //Android默认格式是PixelFormat.OPAQUE，其是不带Alpha值的
        //这句话的作用在于根据原图的效果来设置，避免光晕
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        //建立对应的bitmap
        Bitmap bitmap = Bitmap.createBitmap(width,height,config);
        //建立对应的bitmap画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        //把bitmap画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
