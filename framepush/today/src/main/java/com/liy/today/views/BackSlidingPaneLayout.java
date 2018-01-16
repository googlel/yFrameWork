package com.liy.today.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>Copyright:Copyright(c) 2016</p>
 * <p>Company:上海来伊份电子商务有限公司</p>
 * <p>包名:com.laiyifen.lyfframework.views</p>
 * <p>文件名:lyfen</p>
 * <p>类更新历史信息</p>
 *
 * @todo <a href="mailto:qiujie@laiyifen.com">vernal(邱洁)</a>
 */
public class BackSlidingPaneLayout extends SlidingPaneLayout {
    private List<ViewPager> mViewPagers = new LinkedList<ViewPager> ();

    public BackSlidingPaneLayout(Context context) {
        super (context);
    }

    public BackSlidingPaneLayout(Context context, AttributeSet attrs) {
        super (context, attrs);
    }

    public BackSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        ViewPager mViewPager = getTouchViewPager (mViewPagers, event);
        if (mViewPager != null && mViewPager.getCurrentItem () != 0) {
            return false;
        }
        return super.onInterceptTouchEvent (event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        getAlLViewPager (mViewPagers, this);
        super.onLayout (changed, l, t, r, b);

    }

    private ViewPager getTouchViewPager(List<ViewPager> mViewPagers,
                                        MotionEvent ev) {
        if (mViewPagers == null || mViewPagers.size () == 0) {
            return null;
        }
        Rect mRect = new Rect ();
        for (ViewPager v : mViewPagers) {
            v.getHitRect (mRect);
            if (mRect.contains ((int) ev.getX (), (int) ev.getY ())) {
                return v;
            }
        }
        return null;
    }

    private void getAlLViewPager(List<ViewPager> mViewPagers, ViewGroup parent) {
        int childCount = parent.getChildCount ();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt (i);
            if (child instanceof ViewPager) {
                mViewPagers.add ((ViewPager) child);
            } else if (child instanceof ViewGroup) {
                getAlLViewPager (mViewPagers, (ViewGroup) child);
            }
        }
    }
}
