package com.liy.today.views.screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.liy.today.utils.InputMethodUtils;


/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 1/23/16
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class DetectSoftInputEventFrameLayout extends FrameLayout {

    public static interface OnDispatchTouchEventListener {
        public void dispatchTouchEvent(MotionEvent event);
    }

    private InputMethodUtils.OnSoftInputEventListener mOnSoftInputEventListener;
    private OnDispatchTouchEventListener mDispatchTouchEventListener;
    private boolean mCloseSoftInputOnTouchOutside = true;
    private boolean mInterceptTouchEvent = false;

    private int mPreviousHeight;

    /**
     * @param context context
     */
    public DetectSoftInputEventFrameLayout(Context context) {
        this(context, null);
    }

    /**
     * @param context context
     * @param attrs attrs
     */
    public DetectSoftInputEventFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public DetectSoftInputEventFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param listener listener
     */
    public void setOnDispatchTouchEventListener(OnDispatchTouchEventListener listener) {
        mDispatchTouchEventListener = listener;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mPreviousHeight != 0) {
            if (measureHeight < mPreviousHeight) {
                InputMethodUtils.setIsInputMethodShowing(true);
                if (mOnSoftInputEventListener != null) {
                    mOnSoftInputEventListener.onSoftInputOpened();
                }
                View rootView = getRootView();
                if (rootView != null) {
                   rootView.setBackgroundColor(Color.WHITE);
                }
            } else if (measureHeight > mPreviousHeight) {
                InputMethodUtils.setIsInputMethodShowing(false);
                if (mOnSoftInputEventListener != null) {
                    mOnSoftInputEventListener.onSoftInputClosed();
                }
                View rootView = getRootView();
                if (rootView != null) {
                    rootView.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
        mPreviousHeight = measureHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (InputMethodUtils.isInputMethodShowing() && getContext() instanceof Activity && mCloseSoftInputOnTouchOutside) {
            InputMethodUtils.hideSoftInput((Activity)getContext());
        }
        if (mDispatchTouchEventListener != null) {
            mDispatchTouchEventListener.dispatchTouchEvent(event);
        }
        return mInterceptTouchEvent || super.onTouchEvent(event);
    }

    /**
     * 设置软键盘事件监听
     * @param listener listener
     */
    public void setOnSoftInputEventListener(InputMethodUtils.OnSoftInputEventListener listener) {
        mOnSoftInputEventListener = listener;
    }

    /**
     * 点击软键盘外部区域是否关闭软键盘
     * @param closeSoftInputOnTouchOutside closeSoftInputOnTouchOutside
     */
    public void canCloseSoftInputOnTouchOutside(boolean closeSoftInputOnTouchOutside) {
        mCloseSoftInputOnTouchOutside = closeSoftInputOnTouchOutside;
    }

    /**
     * 点击软键盘外部区域是否拦截点击事件
     * @param interceptTouchEvent interceptTouchEvent
     */
    public void interceptTouchEvent(boolean interceptTouchEvent) {
        mInterceptTouchEvent = interceptTouchEvent;
    }
}
