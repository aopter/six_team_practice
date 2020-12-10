package net.onest.timestoryprj.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class ZXingUtils {
    public static Bitmap createQRImage(String url, int width, int height){
        if (url == null || "".equals(url) || url.length() < 1) {
            return null;
        }
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap addLogo(Bitmap srcBitmap,Bitmap logoBitmap,float logoPercent){
        if (srcBitmap == null){
            return null;
        }
        if (logoPercent <0F || logoPercent >1F){
            logoPercent = 0.2F;
        }
        //获取原图片和logo图片各自的宽、高
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int logoWidth = logoBitmap.getWidth();
        int logoHeight = logoBitmap.getHeight();
        //计算画布缩放的宽高比
        float scaleWidth = srcWidth * logoPercent / logoWidth;
        float scaleHeight = srcHeight * logoPercent / logoHeight;
        //使用canvas绘制，合成图片
        Bitmap bitmap = Bitmap.createBitmap(srcWidth,srcHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawBitmap(srcBitmap,0,0,null);
        canvas.scale(scaleWidth,scaleHeight,srcWidth/2,srcHeight/2);
        canvas.drawBitmap(logoBitmap,srcWidth/2-logoWidth/2,srcHeight/2-logoHeight/2,null);
        return bitmap;
    }
}
