package net.onest.timestoryprj.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import net.onest.timestoryprj.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public class StoryNodeView extends View {
    /**
     * 背景画笔
     */
    private Paint bgPaint;
    /**
     * 前景画笔
     */
    private Paint forePaint;
    /**
     * 选中画笔
     */
    private Paint selectPaint;
    /**
     * 未选中画笔
     */
    private Paint unselectPaint;
    /**
     * 背景颜色
     */
    private int bgColor = Color.parseColor("#FFFFF0");
    /**
     * 前景颜色
     */
    private int foreColor = Color.parseColor("#e8b48c");
    /**
     * 选中颜色
     */
    private int foreTextColor = Color.parseColor("#e06967");
    /**
     * 默认高度
     */
    private int defaultHeight;
    /**
     * 节点文字
     */
    private List<String> nodeList;
    private List<String> nodeProcess;
    private List<Rect> mBounds;
    /**
     * 节点圆的半径
     */
    private int radius;
    /**
     * 文字和节点进度条的top
     */
    private int marginTop;
    /**
     * 两个节点之间的距离
     */
    private int dividWidth;
    /**
     * 选中位置
     */
    private int selectIndex;

    public StoryNodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        radius = SizeUtils.dp2px(context, 9);
        defaultHeight = SizeUtils.dp2px(context, 30);
        marginTop = SizeUtils.dp2px(context, 10);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);

        forePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        forePaint.setColor(foreColor);
        forePaint.setStyle(Paint.Style.FILL);

        unselectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unselectPaint.setColor(bgColor);
        unselectPaint.setTextSize(SizeUtils.sp2px(context, 10));

        selectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectPaint.setColor(foreTextColor);
        selectPaint.setTextSize(20);
        selectPaint.setTextSize(SizeUtils.sp2px(context, 10));
    }


    /**
     * 设置数据
     *
     * @param nodeDatas
     */
    public void setNodeList(List<String> nodeDatas) {
        if (nodeDatas != null) {
            nodeList = nodeDatas;
        }
        nodeProcess = new ArrayList<>();
        int percent = 100 / nodeDatas.size();
        for (int i = 0; i < nodeDatas.size() - 1; i++) {
            nodeProcess.add((i + 1) * percent + " %");
        }
        nodeProcess.add("100%");
        //测量文字所占用的空间
        measureText();
    }

    /**
     * 设置选中位置
     *
     * @param selectIndex
     */
    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        invalidate();
    }

    /**
     * 测量文字的长宽
     */
    private void measureText() {
        mBounds = new ArrayList<>();
        for (int i = 0; i < nodeList.size(); i++) {
            Rect mBound = new Rect();
            unselectPaint.getTextBounds(nodeProcess.get(i), 0, nodeProcess.get(i).length(), mBound);
            mBounds.add(mBound);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (nodeList == null || nodeList.isEmpty()) {
            return;
        }
        bgPaint.setStrokeWidth(SizeUtils.dp2px(getContext(), 8));
        //绘制后置的背景线条
        canvas.drawLine(radius, getHeight() / 2, getWidth() - radius, getHeight() / 2, bgPaint);

        //画节点圆
        //每个圆相隔的距离
        dividWidth = (getWidth() - radius * 2) / (nodeList.size() - 1);
        forePaint.setStrokeWidth(SizeUtils.dp2px(getContext(), 9));
        for (int i = 0; i < nodeList.size(); i++) {
            if (i == selectIndex){
                canvas.drawLine(radius, getHeight() / 2, radius + i * dividWidth, getHeight() / 2, forePaint);
                canvas.drawCircle(radius + i * dividWidth, getHeight() / 2, radius, selectPaint);
            } else if (i <= selectIndex) {
                canvas.drawLine(radius, getHeight() / 2, radius + i * dividWidth, getHeight() / 2, forePaint);
                canvas.drawCircle(radius + i * dividWidth, getHeight() / 2, radius, forePaint);
            } else {
                canvas.drawCircle(radius + i * dividWidth, getHeight() / 2, radius, bgPaint);
            }
        }
        // 画文字
        for (int i = 0; i < nodeList.size(); i++) {
            int currentTextWidth = mBounds.get(i).width();
            if (i == 0) {
                if (i == selectIndex) {
                    canvas.drawText(nodeProcess.get(i), radius - currentTextWidth / 2, getHeight() / 2 - mBounds.get(i).height() / 2 - radius, selectPaint);
                }
            } else if (i == nodeProcess.size() - 1) {
                if (i == selectIndex) {
                    canvas.drawText(nodeProcess.get(i), getWidth() - radius - currentTextWidth / 2, getHeight() / 2 - mBounds.get(i).height() / 2 - radius, selectPaint);
                }
            } else {
                if (i == selectIndex) {
                    canvas.drawText(nodeProcess.get(i), radius + i * dividWidth - currentTextWidth / 2, getHeight() / 2 - mBounds.get(i).height() / 2 - radius, selectPaint);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {//宽高都设置为wrap_content
            setMeasuredDimension(widthSpecSize, defaultHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {//宽设置为wrap_content
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {//高设置为wrap_content
            setMeasuredDimension(widthSpecSize, defaultHeight);
        } else {//宽高都设置为match_parenth或具体的dp值
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onCircleClick(int postion);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX;
        float eventY;
        int i = event.getAction();
        if (i == MotionEvent.ACTION_DOWN) {
        } else if (i == MotionEvent.ACTION_MOVE) {
        } else if (i == MotionEvent.ACTION_UP) {
            eventX = event.getX();
            eventY = event.getY();
            float select = eventX / dividWidth; //计算选中的index
            float xs = select - (int) (select);
            selectIndex = (int) select + (xs > 0.5 ? 1 : 0);
            onClickListener.onCircleClick(selectIndex);
        }
        invalidate();
        return true;
    }
}
