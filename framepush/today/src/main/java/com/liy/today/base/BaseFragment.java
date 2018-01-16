package com.liy.today.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;


public class BaseFragment extends RxFragment {
    public String TAG = "";


    public BaseFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getName();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面
        if (!isHidden()) {
            onFragmentShowed();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i("aaaa", "" + hidden);
        super.onHiddenChanged(hidden);
        if (hidden) {
            onFragmentHidden();
        } else {
            onFragmentShowed();
        }
    }

    protected void onFragmentShowed() {

    }

    protected void onFragmentHidden() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = BaseApplication.getBaseApplication().getRefWatcher();
//        refWatcher.watch(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); //统计页面
    }


}
