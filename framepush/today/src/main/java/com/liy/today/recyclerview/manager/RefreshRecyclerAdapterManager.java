package com.liy.today.recyclerview.manager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.liy.today.recyclerview.RefreshRecyclerView;
import com.liy.today.recyclerview.adapter.RefreshRecyclerViewAdapter;
import com.liy.today.recyclerview.listener.LoadMoreRecyclerListener;
import com.liy.today.recyclerview.listener.OnBothRefreshListener;
import com.liy.today.recyclerview.listener.OnLoadMoreListener;
import com.liy.today.recyclerview.listener.OnPullDownListener;

import java.util.List;


/**
* 作者：qiujie on 16/4/17
* 邮箱：qiujie@laiyifen.com
*/
public class RefreshRecyclerAdapterManager {

    private RefreshRecyclerView recyclerView;
    private RefreshRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerMode mode;

    private OnBothRefreshListener mOnBothRefreshListener;
    private OnPullDownListener mOnPullDownListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private RefreshRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;
    private RefreshRecyclerViewAdapter.OnItemLongClickListener mOnItemLongClickListener;
    private RecyclerView.ItemDecoration mDecor;
    private LoadMoreRecyclerListener loadMoreRecyclerListener;
    private RecyclerView.ItemAnimator mItemAnimator;

    public RefreshRecyclerAdapterManager(
            RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        this.mAdapter = new RefreshRecyclerViewAdapter(adapter);

        if (null == layoutManager){
            throw new NullPointerException("Couldn't resolve a null object reference of LayoutManager");
        }
        this.mLayoutManager = layoutManager;
        if (layoutManager instanceof GridLayoutManager){
            //如果是header或footer，设置其充满整列
            ((GridLayoutManager)layoutManager).setSpanSizeLookup(
                    new HeaderSapnSizeLookUp(mAdapter, ((GridLayoutManager) layoutManager).getSpanCount()));
        }
        this.mLayoutManager = layoutManager;
    }

    private RefreshRecyclerAdapterManager getInstance(){
        return RefreshRecyclerAdapterManager.this;
    }

    public RefreshRecyclerAdapterManager addHeaderView(View v){
        mAdapter.addHeaderView(v);
        return getInstance();
    }

    public RefreshRecyclerAdapterManager addHeaderViewList(List<View> v){
        mAdapter.addHeaderViewList(v);
        return getInstance();
    }

    public RefreshRecyclerAdapterManager addHeaderView(View v, int position){
        mAdapter.addHeaderView(v, position);
        return getInstance();
    }

    public RefreshRecyclerAdapterManager addFooterView(View v){
        mAdapter.addFooterView(v);
        return getInstance();
    }

    public RefreshRecyclerAdapterManager removeHeaderView(View v){
        mAdapter.removeHeader(v);
        return getInstance();
    }

    public RefreshRecyclerViewAdapter getAdapter(){
        return mAdapter;
    }

    public RefreshRecyclerAdapterManager setOnBothRefreshListener(OnBothRefreshListener onBothRefreshListener){
        this.mOnBothRefreshListener = onBothRefreshListener;
        return getInstance();
    }

    public RefreshRecyclerAdapterManager setOnPullDownListener(OnPullDownListener onPullDownListener){
        this.mOnPullDownListener = onPullDownListener;
        return getInstance();
    }

    public RefreshRecyclerAdapterManager setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.mOnLoadMoreListener = onLoadMoreListener;
        return getInstance();
    }

    public RefreshRecyclerAdapterManager removeFooterView(View v){
        mAdapter.removeFooter(v);
        return getInstance();
    }

    public RefreshRecyclerAdapterManager setMode(RecyclerMode mode){
        this.mode = mode;
        return getInstance();
    }

//    public RefreshRecyclerAdapterManager setLayoutManager(RecyclerView.LayoutManager layoutManager){
//        if (null == layoutManager){
//            throw new NullPointerException("Couldn't resolve a null object reference of LayoutManager");
//        }
//        this.mLayoutManager = layoutManager;
//        if (layoutManager instanceof GridLayoutManager){
//            //如果是header或footer，设置其充满整列
//            ((GridLayoutManager)layoutManager).setSpanSizeLookup(
//                    new HeaderSapnSizeLookUp(mAdapter, ((GridLayoutManager) layoutManager).getSpanCount()));
//        }
//        return getInstance();
//    }

    public RefreshRecyclerAdapterManager setOnItemClickListener
            (RefreshRecyclerViewAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
        return getInstance();
    }

    public RefreshRecyclerAdapterManager setOnItemLongClickListener(
            RefreshRecyclerViewAdapter.OnItemLongClickListener onItemLongClickListener){
        this.mOnItemLongClickListener = onItemLongClickListener;
        return getInstance();
    }

    public void onRefreshCompleted(){
        if (null == recyclerView){
            throw new NullPointerException("recyclerView is null");
        }
        if (null == mAdapter){
            throw new NullPointerException("adapter is null");
        }
        if (RecyclerMode.BOTH == mode || RecyclerMode.TOP == mode){
            recyclerView.refreshComplete();
        }
        if (RecyclerMode.BOTH == mode || RecyclerMode.BOTTOM == mode){
            if(null != loadMoreRecyclerListener){
                loadMoreRecyclerListener.onRefreshComplete();
            }
        }

    }

    public RefreshRecyclerView getRecyclerView(){
        if (null == recyclerView){
            throw new NullPointerException("Couldn't resolve a null object reference of RefreshRecyclerView");
        }
        return recyclerView;
    }

    public RefreshRecyclerAdapterManager setItemAnimator(RecyclerView.ItemAnimator itemAnimator){
        this.mItemAnimator = itemAnimator;
        return getInstance();
    }

    public RefreshRecyclerAdapterManager addItemDecoration(RecyclerView.ItemDecoration decor){
        this.mDecor = decor;
        return getInstance();
    }

    public void into(RefreshRecyclerView recyclerView, Context context){
        if (null == recyclerView){
            throw new NullPointerException("Couldn't resolve a null object reference of RefreshRecyclerView");
        }

        mAdapter.putLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setMode(mode);
        //为RefreshRecyclerView添加滚动监听
        loadMoreRecyclerListener = new LoadMoreRecyclerListener(context, mode);
        recyclerView.addOnScrollListener(loadMoreRecyclerListener);
        recyclerView.addItemDecoration(mDecor);
        if (RecyclerMode.BOTH == mode){
            if (null != mOnBothRefreshListener){
                recyclerView.setOnBothRefreshListener(mOnBothRefreshListener);
            }
        } else if (RecyclerMode.TOP == mode){
            if(null != mOnPullDownListener){
                recyclerView.setOnPullDownListener(mOnPullDownListener);
            }
        } else if (RecyclerMode.BOTTOM == mode){
            if (null != mOnLoadMoreListener){
                recyclerView.setOnLoadMoreListener(mOnLoadMoreListener);
            }
        }

        recyclerView.addItemDecoration(mDecor);
        recyclerView.setItemAnimator(mItemAnimator);

        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mAdapter.setOnItemLongClickListener(mOnItemLongClickListener);
        recyclerView.setLayoutManager(mLayoutManager);
        this.recyclerView = recyclerView;
    }

}