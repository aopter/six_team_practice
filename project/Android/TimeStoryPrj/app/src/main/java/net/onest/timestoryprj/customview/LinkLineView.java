package net.onest.timestoryprj.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tinytongtong.tinyutils.ListUtils;
import com.tinytongtong.tinyutils.LogUtils;
import com.tinytongtong.tinyutils.ScreenUtils;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.LinkDataBean;
import net.onest.timestoryprj.entity.LinkLineBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: 连线题的父容器
 * @Author lkl
 * @Date 2020-11-26
 * @Version
 */
public class LinkLineView extends RelativeLayout {
    private static final String TAG = LinkLineView.class.getSimpleName();

    private Context context;

    private List<LinkDataBean> allList = new ArrayList<>();
    private List<LinkDataBean> leftList = new ArrayList<>();
    private List<LinkDataBean> rightList = new ArrayList<>();
    private int size;

    private int cellHeight;
    private int cellWidth;
    private int marginLeft;
    private int marginRight;
    private int marginBottom;

    private List<View> leftTvs = new ArrayList<>();
    private List<View> rightTvs = new ArrayList<>();

    boolean leftSelected;
    boolean rightSelected;
    View tvLeftSelected;
    View tvRightSelected;

    private List<LinkLineBean> linkLineBeanList = new ArrayList<>();
    private List<LinkLineBean> newLinkLineBeanList = new ArrayList<>();

    // 是否可点击
    private boolean isEnabled = true;

    private OnChoiceResultListener onChoiceResultListener;

    private boolean analysisMode;

    public LinkLineView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LinkLineView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinkLineView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LinkLineView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setOnChoiceResultListener(OnChoiceResultListener onChoiceResultListener) {
        this.onChoiceResultListener = onChoiceResultListener;
    }

    private void init(Context context) {
        this.context = context;
    }

    /**
     * 练习
     *
     * @param linkDataBeanList
     */
    public void setData(List<LinkDataBean> linkDataBeanList) {
        if (linkDataBeanList == null || linkDataBeanList.size() == 0) {
            return;
        }
        this.allList.clear();
        leftList.clear();
        rightList.clear();
        this.allList = linkDataBeanList;
        LogUtils.e(TAG, "leftList:" + this.allList);

        // 将数据分为两列
        for (LinkDataBean item : allList) {
            if (0 == item.getCol()) {
                leftList.add(item);
            } else {
                rightList.add(item);
            }
        }

        // 将数据根据行号排序，避免数据错乱
        Collections.sort(leftList, (o1, o2) -> o1.getRow() - o2.getRow());
        Collections.sort(rightList, (o1, o2) -> o1.getRow() - o2.getRow());

        LogUtils.e(TAG, "leftList:" + leftList);
        LogUtils.e(TAG, "rightList:" + rightList);

        size = Math.min(leftList.size(), rightList.size());

        float ratioW = 400 / 1080.0f;
            cellWidth = (int) (ScreenUtils.getScreenW(context) * ratioW);
            cellHeight = (int) (cellWidth * 180 / 1400.0f);
        marginLeft = 0;
        marginRight = 0;
        marginBottom = ScreenUtils.dip2px(context, 10);
        addLeftView();
        addRightView();
    }

    /**
     * 练习
     * 全部黑色
     *
     * @param linkDataBeanList
     */
    public void justShowResult(List<LinkDataBean> linkDataBeanList) {
        this.analysisMode = true;
        setData(linkDataBeanList);

        // view绘制完成后才能获取到宽高
        this.post(() -> {
            List<LinkLineBean> resultList = getResultList();
            // 禁止点击事件
            isEnabled = false;

            newLinkLineBeanList = new ArrayList<>();

            for (int i = 0; i < resultList.size(); i++) {
                // 改变连线的颜色
                resultList.get(i).setColorString(LinkLineBean.COLOR_BLACK);
                // 改变边框的颜色
                leftTvs.get(i).setBackground(context.getResources().getDrawable(R.drawable.bg_black_round_10dp));

                rightTvs.get(i).setBackground(context.getResources().getDrawable(R.drawable.bg_black_round_10dp));

                newLinkLineBeanList.add(resultList.get(i));
            }

            invalidate();
        });
    }

    private void addLeftView() {
        for (int i = 0; i < leftList.size(); i++) {
            LinkDataBean bean = leftList.get(i);
            View view;
                view = generateTextView(bean);
            OnClickListener onClickListener = v -> {
                if (analysisMode) {
                    return;
                }
                if (!isEnabled) {
                    return;
                }
                if (tvLeftSelected != v) {
                    resetLeftTvStatus();
                }
                v.setSelected(true);

                leftSelected = true;
                tvLeftSelected = v;

                if (rightSelected) {
                    resetTvStatus();
                    drawLinkLine();
                }
            };
            view.setOnClickListener(onClickListener);

            // 布局
            LayoutParams lp = new LayoutParams(cellWidth, cellHeight);
            lp.leftMargin = marginLeft;
            lp.topMargin = i * (cellHeight + marginBottom);
            addView(view, lp);
            leftTvs.add(view);
        }
    }

    private void addRightView() {
        for (int i = 0; i < rightList.size(); i++) {
            LinkDataBean bean = rightList.get(i);
            View view= generateTextView(bean);
            OnClickListener onClickListener = v -> {
                if (analysisMode) {
                    return;
                }
                if (!isEnabled) {
                    return;
                }
                if (tvRightSelected != v) {
                    resetRightTvStatus();
                }
                v.setSelected(true);

                rightSelected = true;
                tvRightSelected = v;

                if (leftSelected) {
                    resetTvStatus();
                    drawLinkLine();
                }
            };
            view.setOnClickListener(onClickListener);

            // 布局
            LayoutParams lp = new LayoutParams(cellWidth, cellHeight);
            lp.rightMargin = marginRight;
            lp.topMargin = i * (cellHeight + marginBottom);
            lp.addRule(ALIGN_PARENT_RIGHT);
            addView(view, lp);
            rightTvs.add(view);
        }
    }

    private void resetLeftTvStatus() {
        for (View item : leftTvs) {
            item.setSelected(false);

        }
    }

    private void resetRightTvStatus() {
        for (View item : rightTvs) {
            item.setSelected(false);
        }
    }

    private void resetTvStatus() {
        resetLeftTvStatus();
        resetRightTvStatus();
    }

    /**
     * 绘制连线
     */
    private void drawLinkLine() {

        if (tvLeftSelected == null || tvRightSelected == null) {
            return;
        }

        // 从TextView上获取对应的坐标，进而确定连线的起点和终点的位置
        float startX = tvLeftSelected.getRight();
        float startY = (tvLeftSelected.getTop() + tvLeftSelected.getBottom()) / 2.0f;
        float endX = tvRightSelected.getLeft();
        float endY = (tvRightSelected.getTop() + tvRightSelected.getBottom()) / 2.0f;

        LogUtils.e(TAG, "startX:" + startX + ", startY:" + startY + ", endX:" + endX + ", endY:" + endY);

        if (linkLineBeanList == null) {
            linkLineBeanList = new ArrayList<>();
        }

        LogUtils.e(TAG, "before remove:" + linkLineBeanList);

        newLinkLineBeanList = new ArrayList<>();
        for (LinkLineBean item : linkLineBeanList) {
            newLinkLineBeanList.add(item);
        }

        // 在已绘制好的连线中，去除起点或终点相同的线
        Iterator<LinkLineBean> iterator = newLinkLineBeanList.iterator();
        while (iterator.hasNext()) {
            LinkLineBean bean = iterator.next();
            if (bean != null) {
                if ((startX == bean.getStartX() && startY == bean.getStartY())
                        || (startX == bean.getEndX() && startY == bean.getEndY())
                        || (endX == bean.getStartX() && endY == bean.getStartY())
                        || (endX == bean.getEndX() && endY == bean.getEndY())) {
                    iterator.remove();
                }
            }
        }

        LogUtils.e(TAG, "after remove:" + newLinkLineBeanList);
        LinkLineBean bean = new LinkLineBean(startX, startY, endX, endY);
        int leftIndex = -1;
        for (int i = 0; i < leftTvs.size(); i++) {
            if (tvLeftSelected == leftTvs.get(i)) {
                leftIndex = i;
                break;
            }
        }
        bean.setLeftIndex(leftIndex);
        int rightIndex = -1;
        for (int i = 0; i < rightTvs.size(); i++) {
            if (tvRightSelected == rightTvs.get(i)) {
                rightIndex = i;
                break;
            }
        }
        bean.setRightIndex(rightIndex);
        newLinkLineBeanList.add(bean);

        LogUtils.e(TAG, "after add:" + newLinkLineBeanList);

        // 重置临时变量状态
        leftSelected = false;
        rightSelected = false;
        tvLeftSelected = null;
        tvRightSelected = null;

        // 检查是否所有连线均已完成
        if (newLinkLineBeanList.size() >= size) {
            isEnabled = false;
            verifyResult();
        }

        // 触发dispatchDraw方法，绘制连线
        invalidate();
    }

    private void verifyResult() {
        /**
         * 更新UI，标记出正确的和错误的连线
         */
        drawSelectedLinkLine();

        boolean isRight = true;
        for (LinkLineBean item : newLinkLineBeanList) {
            if (!item.isRight()) {
                isRight = false;
                break;
            }
        }

        String yourAnswer = "";
        if (!ListUtils.isEmpty(newLinkLineBeanList)) {
            Type type = new TypeToken<ArrayList<LinkLineBean>>() {
            }.getType();
            try {
                yourAnswer = new Gson().toJson(newLinkLineBeanList, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (onChoiceResultListener != null) {
            onChoiceResultListener.onResultSelected(isRight, yourAnswer);
        }
    }

    /**
     * 将选择的结果绘制出来，有对有错那种
     */
    private void drawSelectedLinkLine() {
        List<LinkLineBean> resultList = getResultList();

        LogUtils.e(TAG, "resultList:" + resultList);
        for (int i = 0; i < newLinkLineBeanList.size(); i++) {
            newLinkLineBeanList.get(i).setRight(resultList.contains(newLinkLineBeanList.get(i)));
            // 改变连线的颜色
            newLinkLineBeanList.get(i).setColorString(newLinkLineBeanList.get(i).isRight() ? LinkLineBean.COLOR_RIGHT : LinkLineBean.COLOR_WRONG);
            // 改变边框的颜色
            int leftIndex = newLinkLineBeanList.get(i).getLeftIndex();
            if (leftIndex >= 0 && leftIndex < leftTvs.size()) {
                leftTvs.get(leftIndex).setBackground(context.getResources().getDrawable(newLinkLineBeanList.get(i).isRight() ? R.drawable.bg_link_line_green : R.drawable.bg_link_line_red));

            }
            int rightIndex = newLinkLineBeanList.get(i).getRightIndex();
            if (rightIndex >= 0 && rightIndex < rightTvs.size()) {
                rightTvs.get(rightIndex).setBackground(context.getResources().getDrawable(newLinkLineBeanList.get(i).isRight() ? R.drawable.bg_link_line_green : R.drawable.bg_link_line_red));

            }
        }
    }

    /**
     * 获取正确的连线数据
     *
     * @return
     */
    private List<LinkLineBean> getResultList() {
        List<LinkLineBean> resultList = new ArrayList<>(size);
        for (int i = 0; i < leftTvs.size(); i++) {
            // 从TextView上获取对应的起点坐标
            float startX = leftTvs.get(i).getRight();
            float startY = (leftTvs.get(i).getTop() + leftTvs.get(i).getBottom()) / 2.0f;

            LinkDataBean leftBean = leftList.get(i);
            for (int j = 0; j < rightList.size(); j++) {
                if (leftBean.getQ_num() == rightList.get(j).getQ_num()) {
                    float endX = rightTvs.get(j).getLeft();
                    float endY = (rightTvs.get(j).getTop() + rightTvs.get(j).getBottom()) / 2.0f;
                    LinkLineBean linkLineBean = new LinkLineBean(startX, startY, endX, endY);
                    resultList.add(linkLineBean);
                }
            }
        }
        return resultList;
    }

    private TextView generateTextView(LinkDataBean bean) {
        TextView textView = new TextView(context);
        textView.setHeight(30);
        textView.setTextColor(ContextCompat.getColor(context, R.color.black));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        textView.setGravity(Gravity.CENTER);
        textView.setBackground(context.getResources().getDrawable(R.drawable.selector_link_line));
        textView.setTag(bean.getQ_num());
        textView.setText(bean.getContent());
        return textView;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        LogUtils.e(TAG, "dispatchDraw");

        if (linkLineBeanList == null) {
            linkLineBeanList = new ArrayList<>();
        }

        if (newLinkLineBeanList == null) {
            newLinkLineBeanList = new ArrayList<>();
        }

        // 先清除掉原有绘制的线
        for (LinkLineBean item : linkLineBeanList) {
            if (item != null) {
                Paint paint = new Paint();
                paint.setColor(Color.TRANSPARENT);
                paint.setStrokeWidth(ScreenUtils.dip2px(context, 2));
                canvas.drawLine(item.getStartX(), item.getStartY(), item.getEndX(), item.getEndY(), paint);
            }
        }

        for (LinkLineBean item : newLinkLineBeanList) {
            if (item != null) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor(item.getColorString()));
                paint.setStrokeWidth(ScreenUtils.dip2px(context, 2));
                canvas.drawLine(item.getStartX(), item.getStartY(), item.getEndX(), item.getEndY(), paint);
            }
        }

        linkLineBeanList.clear();

        for (LinkLineBean item : newLinkLineBeanList) {
            linkLineBeanList.add(item);
        }
    }

    public interface OnChoiceResultListener {
        void onResultSelected(boolean correct, String yourctAnswer);
    }
}
