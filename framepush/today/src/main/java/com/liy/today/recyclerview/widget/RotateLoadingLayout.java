package com.liy.today.recyclerview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.laiyifen.lyfframework.R;
import com.liy.today.recyclerview.manager.RecyclerMode;


/**
 * 作者：qiujie on 16/4/17
 * 邮箱：qiujie@laiyifen.com
 */
public class RotateLoadingLayout extends RefreshLoadingLayout {


    private ImageView mIvLoad;
    private ObjectAnimator objectAnimator;


    public RotateLoadingLayout(Context context, RecyclerMode mode) {
        super(context, mode);

    }

    @Override
    protected void init() {
        Log.i("aaa", "init");
        View inflate = inflate(mContext, R.layout.loadinglayout, this);
        mIvLoad = (ImageView) inflate.findViewById(R.id.iv_load);
        objectAnimator = ObjectAnimator.ofFloat(mIvLoad, ROTATION, 0, 360);


    }

    @Override
    protected void onRefreshImpl() {


        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setDuration(400);
        objectAnimator.start();
        Log.i("eee", "1");

    }

    @Override
    protected void onResetImpl() {
        Log.i("eee", "2");


    }


}
