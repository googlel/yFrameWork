package com.liy.today.recyclerview.manager;

import android.support.v7.widget.GridLayoutManager;

import com.liy.today.recyclerview.adapter.RefreshRecyclerViewAdapter;


/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class HeaderSapnSizeLookUp extends GridLayoutManager.SpanSizeLookup {

    private RefreshRecyclerViewAdapter mAdapter;
    private int mSpanSize;

    public HeaderSapnSizeLookUp(RefreshRecyclerViewAdapter adapter, int spanSize){
        this.mAdapter = adapter;
        this.mSpanSize = spanSize;
    }

    @Override
    public int getSpanSize(int position) {
        boolean isHeaderOrFooter = mAdapter.isHeader(position) || mAdapter.isFooter(position);
        return isHeaderOrFooter ? mSpanSize : 1;
    }
}
