package com.liy.today.listadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;



import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class DefaultAdapter<Data> extends BaseAdapter implements OnItemClickListener {
    public static final int MORE_VIEW_TYPE = 0;
    public static final int ITEM_VIEW_TYPE = 1;

    protected AbsListView mListView;//和该adapter关联的listView
    private List<BaseHolder> mDisplayedHolders;//用于记录所有显示的holder
    private List<Data> mDatas;//adapter的数据集
    protected Context context;

    private MoreHolder mMoreHolder;
    private volatile boolean mIsLoading;
    public int mPosition;

    public DefaultAdapter(AbsListView listView, List<Data> datas) {
        mDisplayedHolders = new ArrayList<BaseHolder>();
        mListView = listView;
        if (null != listView) {
            listView.setOnItemClickListener (this);
        }
        setData (datas);
    }

    public DefaultAdapter(Context context, AbsListView listView, List<Data> datas) {
        this.context = context;
        mDisplayedHolders = new ArrayList<BaseHolder>();
        mListView = listView;
        if (null != listView) {
            listView.setOnItemClickListener (this);
        }
        setData (datas);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - getHeaderViewCount ();// 此时的position是加上了header的
        onItemClickInner (view, position);

    }

    public int getHeaderViewCount() {
        int count = 0;
        if (mListView != null && mListView instanceof ListView) {
            ListView listView = (ListView) mListView;
            count = listView.getHeaderViewsCount ();
        }
        return count;
    }

    public void onItemClickInner(View view, int position) {

    }

    public void setData(List<Data> datas) {
        mDatas = datas;
    }

    public List<Data> getData() {
        return mDatas;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            if (hasMore ()) {
                return mDatas.size () + 1;// 加1是为了最后加载更多的布局
            } else {
                return mDatas.size ();
            }
        }
        return 0;
    }

    @Override
    public Data getItem(int position) {
        if (mDatas != null && position < mDatas.size ()) {
            return mDatas.get (position);
        }
        return null;
    }

    // 获取item有几种类型，默认是一种类型，这里在加一是为了做加载更多。
    @Override
    public int getViewTypeCount() {
        if (hasMore ()) {
            return super.getViewTypeCount () + 1;// 加1是为了最后加载更多的布局
        } else {
            return super.getViewTypeCount ();
        }
    }

    // 根据position位置返回哪种item展示类型
    @Override
    public int getItemViewType(int position) {
        if (hasMore ()) {
            if (position == getCount () - 1) {
                return MORE_VIEW_TYPE;// 加载更多的布局
            } else {
                return ITEM_VIEW_TYPE;// 普通item的布局
            }
        } else {
            return ITEM_VIEW_TYPE;// 普通item的布局
        }
    }

    //待修改为抽象
    public boolean hasMore() {
        return true;
    }

    //如果hasMore()返回false，直接return null,待修改为抽象
    public List<Data> onLoadMore() {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.mPosition = position;
        BaseHolder<Data> holder;
        if (convertView != null && convertView.getTag () instanceof BaseHolder) {
            holder = (BaseHolder<Data>) convertView.getTag ();
        } else {
            if (getItemViewType (position) == MORE_VIEW_TYPE) {
                holder = getMoreHolder ();
            } else {
                holder = getHolder ();
            }
        }
        if (getItemViewType (position) == ITEM_VIEW_TYPE) {
            holder.setData (mDatas.get (position));
        }
        mDisplayedHolders.add (holder);
        return holder.getRootView ();
    }

    protected abstract BaseHolder getHolder();

    public BaseHolder getMoreHolder() {
        if (mMoreHolder == null) {
            mMoreHolder = new MoreHolder (this, hasMore ());
        }
        return mMoreHolder;
    }

    public void loadMore() {
        //防止重复加载
        if (!mIsLoading) {
            mIsLoading = true;
            Observable.create (new Observable.OnSubscribe<List<Data>> () {
                @Override
                public void call(Subscriber<? super List<Data>> subscriber) {
                    subscriber.onNext (onLoadMore ());
                    subscriber.onCompleted ();
                }
            }).subscribeOn (Schedulers.io ())
                    .observeOn (AndroidSchedulers.mainThread ())
                    .unsubscribeOn (Schedulers.io ())
                    .subscribe (new Observer<List<Data>>() {
                        @Override
                        public void onCompleted() {
                            notifyDataSetChanged ();
                            mIsLoading = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            notifyDataSetChanged ();
                            mIsLoading = false;
                        }

                        @Override
                        public void onNext(List<Data> datas) {
                            if (datas == null) {
                                getMoreHolder ().setData (MoreHolder.ERROR);
                            } else if (datas.size () < 20) {
                                getMoreHolder ().setData (MoreHolder.NO_MORE);
                            } else {
                                getMoreHolder ().setData (MoreHolder.HAS_MORE);
                            }
                            if (datas != null) {
                                if (getData () != null) {
                                    getData ().addAll (datas);
                                } else {
                                    setData (datas);
                                }
                            }


                        }
                    });


        }
    }

    public List<BaseHolder> getDisplayedHolders() {
        synchronized (mDisplayedHolders) {
            return new ArrayList<BaseHolder>(mDisplayedHolders);
        }
    }

}
