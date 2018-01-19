package com.liy.today.base;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.laiyifen.lyfframework.R;
import com.liy.today.views.action.Action;
import com.liy.today.views.action.ActionTitleBar;
import com.liy.today.views.action.ImageAction;
import com.liy.today.views.action.KitKatStatusBarPlaceholder;
import com.liy.today.views.action.TextAction;
import com.liy.today.ysonw.snowingview.widgets.SnowingView;
import com.umeng.analytics.MobclickAgent;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class ActionBarActivity extends BaseActivity {

    private RelativeLayout mRootView;
    private FrameLayout mContentView;
    public ActionTitleBar mTitleBar;
    private ProgressBar mProgressBar;
    public TextAction mReturnAction;
    public SnowingView snowingView;
    public String TAG;
    public KitKatStatusBarPlaceholder mKitKatStatusBarPlaceholder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = (RelativeLayout) View.inflate(this, R.layout.action_bar_activity, null);
        mContentView = (FrameLayout) mRootView.findViewById(R.id.content_view);
        mTitleBar = (ActionTitleBar) mRootView.findViewById(R.id.title_bar_switcher);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progress_bar);
        snowingView = (SnowingView) mRootView.findViewById(R.id.snowing_view);
        mKitKatStatusBarPlaceholder = (KitKatStatusBarPlaceholder) mRootView.findViewById(R.id.statusBarPlaceholder);


        mTitleBar.setTitle(getTitle());
        addDefaultReturnAction();
        TAG = getClass().getSimpleName();

        Window window = getWindow();

        // 虚拟导航栏透明
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        super.setContentView(mRootView);//初始化页面显示
    }

    public void setKatStatusBarColor(int color) {
        mKitKatStatusBarPlaceholder.setBackgroundColor(color);
    }


    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        buildContentView(view, new RelativeLayout.LayoutParams(-1, -1), false);
    }

    @Override
    public void setContentView(View view) {
        buildContentView(view, new RelativeLayout.LayoutParams(-1, -1), false);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        buildContentView(view, params, false);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        buildContentView(view, params, true);
    }

    @Override
    public void  setTitle(CharSequence title) {
        super.setTitle(title);
        if (mTitleBar != null) mTitleBar.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        if (mTitleBar != null) mTitleBar.setTitle(titleId);
    }

    private View buildContentView(View contentView, ViewGroup.LayoutParams params, boolean isAdd) {
        if (mTitleBar != null) {
            if (!isAdd) {
                try {
                    mContentView.removeAllViews();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
            if (contentView != null) {
                mContentView.addView(contentView, params);
            }
            return mRootView;
        } else {
            return contentView;
        }
    }

    public View getContentView() {
        if (mContentView != null && mContentView.getChildCount() > 0) {
            return mContentView.getChildAt(0);
        }
        return null;
    }

    /**
     * 获取ActionTitleBar
     *
     * @return ActionTitleBar
     */
    public ActionTitleBar getActionTitleBar() {
        return mTitleBar;
    }

    public void setHeadView(View view){
        getActionTitleBar().removeAllViews();
        getActionTitleBar().addView(view);
    }
    /**
     * 默认显示ActionBar，此处设置隐藏ActionBar
     */
    public void hideActionbar() {
        mTitleBar.hide();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置 actionbar背景颜色
     */
    public void setActionTitleBarColor(int color) {

        getActionTitleBar().setBackgroundColor(color);

    }


    /**
     * 是否actionbar显示出来了
     *
     * @return boolean
     */
    public boolean isActionBarVisible() {
        return mTitleBar.getVisibility() == View.VISIBLE;
    }

    /**
     * 获取TitleTxtView
     *
     * @return View
     */
    public View getTitleBarTxt() {
        if (mTitleBar != null) {
            return mTitleBar.getTitleTxtView();
        }
        return null;
    }

    public TextAction getmReturnAction() {
        return mReturnAction;
    }

    public void addDefaultReturnAction() {
        if (mReturnAction == null) {
            mReturnAction = new TextAction(this, "", R.drawable.icon_back_orange, Action.LEFT_OF);
            mReturnAction.setHorizontalRule(Action.LEFT_OF);
            mReturnAction.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else {
                        finish();
                    }

                }
            });
        }
        if (mTitleBar != null) mTitleBar.addAction(mReturnAction);
    }


    public void addReturnAction() {
        getActionTitleBar().removeAllLeftAction();
        ImageAction backAction = new ImageAction(this, R.drawable.icon_back_white);
        backAction.setHorizontalRule(Action.LEFT_OF);
        backAction.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (mTitleBar != null) {
            mTitleBar.addAction(backAction);
        }
    }

    /**
     * 设置进度(0-100)
     *
     * @param progress progress
     */
    public void setTitleProgress(float progress) {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            return;
        }
        int realProgress = (int) (progress * mProgressBar.getMax() / 100);
        mProgressBar.setProgress(realProgress);
        if (realProgress > 0 && realProgress < mProgressBar.getMax()) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 设置进度，会有过度动画效果(0-100)
     *
     * @param aimProgress animProgress
     */
    public void setTitleProgressSmooth(final int aimProgress) {
        mProgressBar.clearAnimation();

        final float startProgress = mProgressBar.getProgress() * 100f / mProgressBar.getMax();
        if (startProgress >= aimProgress) {
            setTitleProgress(startProgress);
            return;
        }
        if (aimProgress >= 100) {
            setTitleProgress(100);
            return;
        }

        Animation smoothAnim = new Animation() {
            float increase = aimProgress - startProgress;

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                setTitleProgress(interpolatedTime * increase + startProgress);
            }
        };
        smoothAnim.setInterpolator(new DecelerateInterpolator(1));
        smoothAnim.setDuration(2000);
        smoothAnim.setFillAfter(true);
        mProgressBar.startAnimation(smoothAnim);
    }

    /**
     * 设置title和CompoundDrawable
     *
     * @param title title
     * @param resId resId
     * @param rule  rule {@link Action#LEFT_OF or @link Action#RIGHT_OF}
     */
    public void setTitleAndCompoundDrawable(CharSequence title, int resId, int rule) {
        if (mTitleBar != null) mTitleBar.setTitleAndCompoundDrawable(title, resId, rule);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
