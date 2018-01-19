package com.liy.today.views.action;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laiyifen.lyfframework.R;
import com.liy.today.utils.ViewUtils;


/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class Action<T extends View> {

    private T mActionView;
    private Object mTag;
    private boolean mEnable = true;
    private int mHorizontalRule = RIGHT_OF;
    private FrameLayout mContain;

    /**
     * Rule that aligns left edge.
     */
    public static final int LEFT_OF = 0;
    /**
     * Rule that aligns right edge.
     */
    public static final int RIGHT_OF = 1;

    /**
     * Rule that img or txt align top edge
     */
    public static final int TOP_OF = 2;

    /**
     * Rule that img or txt align bottom edge
     */
    public static final int BOTTOM_OF = 3;

    /**
     * Action init
     * @param action action
     */
    public Action(@NonNull T action, String badgeNumber) {
        addViewToLayout(action, badgeNumber);
    }

    public Action(@NonNull T action) {
        this(action, null);
    }

    private void addViewToLayout(T child, String badgeNumber) {
        if(badgeNumber==null){
            mActionView = child;
            return;
        }
        mContain = new FrameLayout(child.getContext());
        mContain.addView(child,  new FrameLayout.LayoutParams(-2, -1, Gravity.CENTER));

        TextView redPointView = new TextView(child.getContext());
        redPointView.setTextSize(12);
        redPointView.setGravity(Gravity.CENTER);
        redPointView.setTextColor(Color.WHITE);
        redPointView.setText(badgeNumber);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2, Gravity.TOP| Gravity.RIGHT);
        if(TextUtils.isEmpty(badgeNumber)){
            redPointView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.common_ic_red_point, 0);
            params.topMargin = params.rightMargin = (int) ViewUtils.dp2Px(child.getContext(), 6);
            mContain.addView(redPointView, params);
        }else{
            redPointView.setBackgroundResource(R.mipmap.common_ic_red_point_big);
            int size = (int) ViewUtils.dp2Px(child.getContext(), 18);
            redPointView.setMinimumWidth(size);
            params.height = size;
            params.topMargin = params.rightMargin = (int) ViewUtils.dp2Px(child.getContext(), 4);
            mContain.addView(redPointView, params);
        }
        mActionView = child;
    }

    /**
     * 获取Context
     * @return context
     */
    public Context getContext() {
        return mActionView.getContext();
    }

    /**
     * 设置是否开启此动作按钮
     * @param enable true if yes
     */
    public void setEnable(boolean enable) {
        mEnable = enable;
        if (isVisible()) {
            mActionView.setEnabled(enable);
        }
    }

    /**
     * 隐藏按钮
     * @param withPlace 如果仍然占据空间，设置为true
     */
    public void hide(boolean withPlace) {
        mActionView.setVisibility(withPlace ? View.INVISIBLE : View.GONE);
        mActionView.setEnabled(false);
    }

    /**
     * 隐藏按钮，并让出所占用的空间
     */
    public void hide() {
        hide(false);
    }

    /**
     * 显示按钮
     */
    public void show() {
        setVisible(true);
    }

    /**
     * 设置是否显示此动作按钮
     * @param visible true if yes
     */
    public void setVisible(boolean visible) {
        mActionView.setVisibility(visible ? View.VISIBLE : View.GONE);
        mActionView.setEnabled(visible & mEnable);
    }

    /**
     * 是否可见
     * @return true if yes
     */
    public boolean isVisible() {
        return mActionView.getVisibility() == View.VISIBLE;
    }

    /**
     * 是否开启
     * @return true if yes
     */
    public boolean isEnable() {
        return mEnable;
    }

    /**
     * 设置附带信息
     * @param tag tag
     */
    public void setTag(Object tag) {
        mTag = tag;
    }

    /**
     * 获取附带信息
     * @return tag
     */
    public Object getTag() {
        return mTag;
    }

    /**
     * 获取ActionView
     * @return mActionView
     */
    public View getParentView() {
        return mContain == null ? mActionView : mContain;
    }

    /**
     * getView
     * @return T
     */
    public T getView() {
        return mActionView;
    }

    public Action setClick(View.OnClickListener listener){
        mActionView.setOnClickListener(listener);
        return this;
    }

    /**
     * 获取Action被放置方向
     * @return int {@link #LEFT_OF or @link #RIGHT_OF}
     */
    public int getHorizontalRule() {
        return mHorizontalRule;
    }

    /**
     * 设置Action被放置方向
     * @param rule {@link #LEFT_OF or @link #RIGHT_OF}
     */
    public void setHorizontalRule(int rule) {
        mHorizontalRule = rule;
    }

}
