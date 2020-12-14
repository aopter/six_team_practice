package net.onest.timestoryprj.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import com.tinytongtong.tinyutils.LogUtils;

public class CusKRelativeLayout extends RelativeLayout {
    public CusKRelativeLayout(Context context) {
        super(context);
    }

    public CusKRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusKRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public CusKRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int x2 = (int) ev.getX();
                float x3 = x - x2;
                LogUtils.d("长度",x3+"");
                if (x3 > 0) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
        return intercepted;
    }
}
