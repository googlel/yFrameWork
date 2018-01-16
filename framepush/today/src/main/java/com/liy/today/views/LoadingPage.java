package com.liy.today.views;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.laiyifen.lyfframework.R;


public class LoadingPage extends FrameLayout {
    private static final int STATE_UNLOADED = 0;//未知状态
    private static final int STATE_LOADING = 1;//加载状态
    private static final int STATE_ERROR = 3;//加载完毕，但是出错状态
    private static final int STATE_EMPTY = 4;//加载完毕，但是没有数据状态
    private static final int STATE_SUCCEED = 5;//加载成功
    public static final int STATE_NONETWORK = 6;//无网络

    private View mLoadingView;//加载时显示的View
    private View mErrorView;//加载出错显示的View
    private View mEmptyView;//加载没有数据显示的View
    private View mSucceedView;//加载成功显示的View
    private View mNoNetworkView;//无网络view

    private int mState;//当前的状态，显示需要根据该状态判断


    public void setmState(int mState) {
        this.mState = mState;
    }

    private Context context;

    public LoadingPage(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    private void init() {
        setBackgroundColor(Color.parseColor("#ffffff"));//设置背景
        mState = STATE_UNLOADED;//初始化状态

        //创建对应的View，并添加到布局中
        mLoadingView = createLoadingView();

        if (null != mLoadingView) {
            addView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        mErrorView = createErrorView();
        mErrorView.setVisibility(INVISIBLE);
        if (null != mErrorView) {
            addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        mEmptyView = createEmptyView();
        mEmptyView.setVisibility(INVISIBLE);
        if (null != mEmptyView) {
            addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        mNoNetworkView = createNoNetworkErrorView();
        mNoNetworkView.setVisibility(INVISIBLE);
        if (null != mNoNetworkView) {
            addView(mNoNetworkView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        //显示对应的View
        showPageSafe();
    }

    /**
     * 线程安全的方法
     */
    private void showPageSafe() {
        post(() -> showPage());
    }


    public LoadingPage showPage(LoadResult loadResult) {
        mState = loadResult.getValue();
        showPage();
        return this;
    }

    public LoadingPage showPage(LoadResult loadResult, View succeedView) {
        mSucceedView = succeedView;
        showPage(loadResult);
        return this;

    }

    /**
     * 显示对应的View
     */
    public void showPage() {
        if (null != mLoadingView) {
            mLoadingView.setVisibility(mState == STATE_UNLOADED || mState == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }
        if (null != mErrorView) {
            mErrorView.setVisibility(mState == STATE_ERROR ? View.VISIBLE : View.INVISIBLE);
        }
        if (null != mEmptyView) {
            mEmptyView.setVisibility(mState == STATE_EMPTY ? View.VISIBLE : View.INVISIBLE);
        }
        if (null != mNoNetworkView) {
            mNoNetworkView.setVisibility(mState == STATE_NONETWORK ? View.VISIBLE : View.INVISIBLE);
        }

        // 只有数据成功返回了，才知道成功的View该如何显示，因为该View的显示依赖加载完毕的数据
        if (mState == STATE_SUCCEED) {
            if (mSucceedView != null) {
                removeSelfFromParent(mSucceedView);
                addView(mSucceedView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        }

        if (null != mSucceedView) {
            mSucceedView.setVisibility(mState == STATE_SUCCEED ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * 把自身从父View中移除
     */
    public void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * 恢复状态
     */
    public void reset() {
        mState = STATE_UNLOADED;
        showPageSafe();
    }

    /**
     * 是否需要恢复状态
     */
    protected boolean needReset() {
        return mState == STATE_ERROR || mState == STATE_EMPTY || mState == STATE_NONETWORK;
    }


    protected View createLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.loading_view, null);
    }

    protected View createEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.empty_view, null);
    }

    protected View createErrorView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.error_view, null);
    }

    protected View createNoNetworkErrorView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.nonet_view, null);
        LinearLayout ll_retry = (LinearLayout) view.findViewById(R.id.ll_retry);
        ll_retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                try {
                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    } else {
                        intent = new Intent();
                        ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                        intent.setComponent(component);
                        intent.setAction("android.intent.action.VIEW");
                    }
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        return view;
    }

    public enum LoadResult {
        ERROR(3), EMPTY(4), SUCCEED(5), NONETWORK(6);
        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
