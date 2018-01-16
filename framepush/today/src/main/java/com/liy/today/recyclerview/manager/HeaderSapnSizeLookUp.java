package com.liy.today.recyclerview.manager;

import android.support.v7.widget.GridLayoutManager;

import com.liy.today.recyclerview.adapter.RefreshRecyclerViewAdapter;


/**
 * 作者：qiujie on 16/4/17
 * 邮箱：qiujie@laiyifen.com
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
