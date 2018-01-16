package com.liy.today.views.screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.liy.today.utils.InputMethodUtils;


/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 1/23/16
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class DetectSoftInputFrameLayout extends FrameLayout {

    /**
     * OnDispatchTouchEventListener
     */
    public static interface OnDispatchTouchEventListener {
        /**
         * @param event event
         */
        public void dispatchTouchEvent(MotionEvent event);
    }

    private OnDispatchTouchEventListener mDispatchTouchEventListener;
    private boolean mCloseSoftInputOnTouchOutside = true;
    private int mUsableHeightPrevious;

    /**
     * MonitorSizeEventFrameLayout
     *
     * @param context context
     */
    public DetectSoftInputFrameLayout(Context context) {
        super(context);
        post(new Runnable() {
            @Override
            public void run() {
                detect();
            }
        });
    }

    /**
     * MonitorSizeEventFrameLayout
     *
     * @param context context
     * @param attrs   attrs
     */
    public DetectSoftInputFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        post(new Runnable() {
            @Override
            public void run() {
                detect();
            }
        });
    }

    private void detect() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
    }

    /**
     * @param listener listener
     */
    public void setOnDispatchTouchEventListener(OnDispatchTouchEventListener listener) {
        mDispatchTouchEventListener = listener;
    }


    /**
     * 点击软键盘外部区域是否关闭软键盘
     * @param closeSoftInputOnTouchOutside closeSoftInputOnTouchOutside
     */
    public void canCloseSoftInputOnTouchOutside(boolean closeSoftInputOnTouchOutside) {
        mCloseSoftInputOnTouchOutside = closeSoftInputOnTouchOutside;
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != mUsableHeightPrevious) {
            int usableHeightSansKeyboard = getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                InputMethodUtils.setIsInputMethodShowing(true);
            } else {
                InputMethodUtils.setIsInputMethodShowing(false);
            }
            mUsableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (InputMethodUtils.isInputMethodShowing() && getContext() instanceof Activity && mCloseSoftInputOnTouchOutside) {
            InputMethodUtils.hideSoftInput((Activity)getContext());
        }
        if (mDispatchTouchEventListener != null) {
            mDispatchTouchEventListener.dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }
}
