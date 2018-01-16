package com.liy.today.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.ViewGroup;

import com.liy.today.manager.AppManager;
import com.liy.today.statistic.UBTService;
import com.liy.today.views.BackSlidingPaneLayout;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;


public class BaseActivity extends RxAppCompatActivity implements SlidingPaneLayout.PanelSlideListener {

    private boolean mIsDestoryed;
    public WeakHandler mWeakHandler;


    /**
     * Instances of stat
     * ic inner classes do not hold an implicit
     * reference to their outer class.
     * 避免Handler引起的内存泄露，Activity在Destroy后，这个Handler中的Message不会被执行
     */
    public static class WeakHandler extends Handler {
        private final WeakReference<Context> mContext;

        /**
         * WeakHandler
         *
         * @param context context
         */
        public WeakHandler(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void dispatchMessage(Message msg) {
            Context context = mContext.get();
            if (context != null) {
                if (context instanceof BaseActivity && ((BaseActivity) context).isDestoryed()) {
                    return;
                } else if (Build.VERSION.SDK_INT >= 17
                        && context instanceof Activity
                        && ((Activity) context).isDestroyed()) {
                    return;
                }
                super.dispatchMessage(msg);
            }
        }
    }

    private List<ViewPager> mViewPagers = new LinkedList<ViewPager>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().setBackgroundDrawableResource(R.mipmap.window_bg);
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }




        AppManager.getAppManager().addActivity(this);
        mWeakHandler = new WeakHandler(this);
        startUBTService();
        initSwipeBackFinish();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * 根据生命周期获取是否Destory
     *
     * @return isDestoryed
     */
    public boolean isDestoryed() {
        return mIsDestoryed;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        AppManager.getAppManager().removeActivity(this);
        mIsDestoryed = true;
    }

    private void initSwipeBackFinish() {
        if (isSupportSwipeBack() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            BackSlidingPaneLayout slidingPaneLayout = new BackSlidingPaneLayout(this);
            //通过反射改变mOverhangSize的值为0，这个mOverhangSize值为菜单到右边屏幕的最短距离，默认
            //是32dp，现在给它改成0
            try {
                //属性
                Field f_overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
                f_overHang.setAccessible(true);
                f_overHang.set(slidingPaneLayout, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            slidingPaneLayout.setPanelSlideListener(this);
            slidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));

            View leftView = new View(this);
            leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            slidingPaneLayout.addView(leftView, 0);

            ViewGroup decor = (ViewGroup) getWindow().getDecorView();
            ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
            decorChild.setBackgroundColor(getResources().getColor(android.R.color.white));
            decor.removeView(decorChild);
            decor.addView(slidingPaneLayout);
            slidingPaneLayout.addView(decorChild, 1);
        }
    }

    /**
     * 是否支持滑动返回
     *
     * @return
     */
    protected boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onPanelClosed(View view) {

    }

    @Override
    public void onPanelOpened(View view) {
        finish();
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }

    private void startUBTService() {
        Intent intent = new Intent();
        intent.setClass(this, UBTService.class);
        startService(intent);
    }
}
