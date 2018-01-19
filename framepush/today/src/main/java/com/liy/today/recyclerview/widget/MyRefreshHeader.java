package com.liy.today.recyclerview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laiyifen.lyfframework.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class MyRefreshHeader extends FrameLayout implements PtrUIHandler {
    private int state;
    private int headerViewHeight;

    private static final int DONE = 0;
    private static final int PULL_TO_REFRESH = 1;
    private static final int RELEASE_TO_REFRESH = 2;
    private static final int REFRESHING = 3;
    private int mFirstVisibleItem;
    private boolean isRecord;
    private boolean isEnd;
    private boolean isRefreable;
    private FrameLayout mAnimContainer;
    private PointerView mAutoHomeAnim;
    private TextView tv_pull_to_refresh;
    private TextView tv_time;
    private AutoHome mAutoHome;
    private View headerView;
    private int height;
    private String mLastUpdateTimeKey;
    private boolean mShouldShowLastUpdate;
    private long mLastUpdateTime = -1;
    private final static String KEY_SharedPreferences = "cube_ptr_classic_last_update";
    private static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private LastUpdateTimeUpdater mLastUpdateTimeUpdater = new LastUpdateTimeUpdater();
    private ObjectAnimator objectAnimator;

    public MyRefreshHeader(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context ctx) {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.autohome_item, this);
//        headerView = (LinearLayout) LayoutInflater.from(ctx).inflate(R.layout.autohome_item, null, false);
        headerViewHeight = headerView.getMeasuredHeight();
        int height = headerView.getHeight();
//        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) headerView.getLayoutParams();
//
//        int heightl=layoutParams.height;


        Log.i("aaa", headerViewHeight + "---" + height + "---");
        mAutoHome = (AutoHome) headerView.findViewById(R.id.auto_home);
        tv_pull_to_refresh = (TextView) headerView.findViewById(R.id.tv_pull_to_refresh);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);
        mAnimContainer = (FrameLayout) headerView.findViewById(R.id.anim_container);
        mAutoHomeAnim = (PointerView) headerView.findViewById(R.id.anim_pointer);
        measureView(headerView);
        state = DONE;
        isEnd = true;
        isRefreable = false;

        objectAnimator = ObjectAnimator.ofFloat(mAutoHomeAnim, View.ROTATION, 0, 360);



    }

    public MyRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public MyRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);


        height = Math.max(p.height, headerView.getMeasuredHeight());
//       headerView.getMeasuredHeight();
        Log.i("aaa---头高", height + "");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mLastUpdateTimeUpdater != null) {
            mLastUpdateTimeUpdater.stop();
        }
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        Log.i("aaa--------life", "1");
        mAutoHome.setVisibility(VISIBLE);
        mAnimContainer.setVisibility(GONE);
        objectAnimator.cancel();
        if (frame.isPullToRefresh()) {
            tv_pull_to_refresh.setText("下拉刷新");
        } else {
            tv_pull_to_refresh.setText("下拉刷新");
        }
        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
//        tv_pull_to_refresh.setText("下拉刷新");
        Log.i("aaa--------life", "2");
        mAutoHome.setVisibility(VISIBLE);
        mAnimContainer.setVisibility(GONE);

        objectAnimator.cancel();
        mLastUpdateTimeUpdater.start();

        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
        if (frame.isPullToRefresh()) {
            tv_pull_to_refresh.setText("下拉刷新");
        } else {
            tv_pull_to_refresh.setText("下拉刷新");
        }



    }


    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mAutoHome.setVisibility(GONE);
        mAnimContainer.setVisibility(VISIBLE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setDuration(400);

        objectAnimator.start();
        if(frame.isPullToRefresh()){

        tv_pull_to_refresh.setText("正在刷新");
        }else{
            tv_pull_to_refresh.setText("正在刷新");

        }

        Log.i("aaa--------life", "3");
        mShouldShowLastUpdate = false;
        tryUpdateLastUpdateTime();
        mLastUpdateTimeUpdater.stop();

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        objectAnimator.cancel();
        mAutoHome.setVisibility(VISIBLE);
        mAnimContainer.setVisibility(GONE);

        tv_pull_to_refresh.setText("刷新完成");


        Log.i("aaa--------life", "4");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(KEY_SharedPreferences, 0);
        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = new Date().getTime();
            sharedPreferences.edit().putLong(mLastUpdateTimeKey, mLastUpdateTime).commit();

        }

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
//        float currentPercent = ptrIndicator.getCurrentPosY();
//        mAnimContainer.setVisibility(GONE);
//        mAutoHome.setVisibility(VISIBLE);
//
//        objectAnimator.cancel();

        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);

            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);

            }
        }
        float currentHeight = (-height + currentPos / 2);
        float currentProgress = 1 + currentHeight / height;

        mAutoHome.setCurrentProgress(currentProgress);
        mAutoHome.postInvalidate();
    }

    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            tv_pull_to_refresh.setText("释放刷新");
        }
    }

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {

        if (frame.isPullToRefresh()) {
            tv_pull_to_refresh.setText("下拉刷新");

        } else {
            tv_pull_to_refresh.setText("下拉刷新");

        }
    }

    private void tryUpdateLastUpdateTime() {
        if (TextUtils.isEmpty(mLastUpdateTimeKey) || !mShouldShowLastUpdate) {
            tv_time.setVisibility(GONE);
        } else {
            String time = getLastUpdateTime();
            if (TextUtils.isEmpty(time)) {
                tv_time.setVisibility(GONE);
            } else {
                tv_time.setVisibility(VISIBLE);
                tv_time.setText(time);
            }
        }
    }

    private String getLastUpdateTime() {

        if (mLastUpdateTime == -1 && !TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = getContext().getSharedPreferences(KEY_SharedPreferences, 0).getLong(mLastUpdateTimeKey, -1);
        }
        if (mLastUpdateTime == -1) {
            return null;
        }
        long diffTime = new Date().getTime() - mLastUpdateTime;
        int seconds = (int) (diffTime / 1000);
        if (diffTime < 0) {
            return null;
        }
        if (seconds <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getContext().getString(R.string.cube_ptr_last_update));

        if (seconds < 60) {
            sb.append(seconds + getContext().getString(R.string.cube_ptr_seconds_ago));
        } else {
            int minutes = (seconds / 60);
            if (minutes > 60) {
                int hours = minutes / 60;
                if (hours > 24) {
                    Date date = new Date(mLastUpdateTime);
                    sb.append(sDataFormat.format(date));
                } else {
                    sb.append(hours + getContext().getString(R.string.cube_ptr_hours_ago));
                }

            } else {
                sb.append(minutes + getContext().getString(R.string.cube_ptr_minutes_ago));
            }
        }
        return sb.toString();
    }


    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mLastUpdateTimeKey = key;
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        setLastUpdateTimeKey(object.getClass().getName());
    }

    private class LastUpdateTimeUpdater implements Runnable {

        private boolean mRunning = false;

        private void start() {
            if (TextUtils.isEmpty(mLastUpdateTimeKey)) {
                return;
            }
            mRunning = true;
            run();
        }

        private void stop() {
            mRunning = false;
            removeCallbacks(this);
        }

        @Override
        public void run() {
            tryUpdateLastUpdateTime();
            if (mRunning) {
                postDelayed(this, 1000);
            }
        }
    }
}
