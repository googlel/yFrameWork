package com.liy.today.listadapter;

import android.content.Context;
import android.view.View;
/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public abstract class
BaseHolder<Data> {
    protected View mRootView;
    protected int mPosition;
    protected Data mData;
    protected Context context;


    public BaseHolder(Context context) {
        this.context = context;
        mRootView = initView();
        mRootView.setTag(this);
    }

    public BaseHolder() {
        mRootView = initView();
        mRootView.setTag(this);
    }

    public View getRootView() {
        return mRootView;
    }

    public void setData(Data data) {
        mData = data;
        refreshView();
    }

    public Data getData() {
        return mData;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }


    protected abstract View initView();


    public abstract void refreshView();


    public void recycle() {

    }


}
