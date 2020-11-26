package net.onest.timestoryprj.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class CusTextView extends TextView {

    public CusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont();
    }

    public CusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public CusTextView(Context context) {
        super(context);
        initFont();
    }

    private void initFont() {
        if (!isInEditMode()) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/custom_fontt.ttf");
            setTypeface(typeface);
        }
    }
}
