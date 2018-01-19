package com.liy.today.recyclerview.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.liy.today.recyclerview.manager.RecyclerMode;


/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public abstract class RefreshLoadingLayout extends FrameLayout {

    static final int ROTATION_ANIMATION_DURATION = 1200;

    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

    protected Context mContext;
    protected RecyclerMode mode;

    public RefreshLoadingLayout(Context context, RecyclerMode mode) {
        super(context);
        this.mContext = context;
        this.mode = mode;

        init();

        reset();
    }

    protected void init() {
    }

    public final void onRefresh() {
        onRefreshImpl();
    }

    public void reset() {
        onResetImpl();
    }

    protected abstract void onRefreshImpl();

    protected abstract void onResetImpl();

}
