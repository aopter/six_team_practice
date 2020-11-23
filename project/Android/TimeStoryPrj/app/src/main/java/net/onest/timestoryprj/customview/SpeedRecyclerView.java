package net.onest.timestoryprj.customview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 控制fling速度
 */
public class SpeedRecyclerView extends RecyclerView {
    private static final float FLING_SCALE_DOWN_FACTOR = 0.5f; // 减速因子
    private static final int FLING_MAX_VELOCITY = 8000; // 最大顺时滑动速度

    public SpeedRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SpeedRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX = solveVelocity(velocityX);
        velocityY = solveVelocity(velocityY);
        return super.fling(velocityX, velocityY);
    }

    private int solveVelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, FLING_MAX_VELOCITY);
        } else {
            return Math.max(velocity, -FLING_MAX_VELOCITY);
        }
    }
}
