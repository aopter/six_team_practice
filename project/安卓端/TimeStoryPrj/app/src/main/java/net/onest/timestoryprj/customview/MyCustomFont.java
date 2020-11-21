package net.onest.timestoryprj.customview;

import android.app.Application;
import android.graphics.Typeface;

import java.lang.reflect.Field;

// 可改变整个项目的字体，备用
public class MyCustomFont extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initTypeface();
    }

    private void initTypeface() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/custom_font.ttf");
        try {
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null, typeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
