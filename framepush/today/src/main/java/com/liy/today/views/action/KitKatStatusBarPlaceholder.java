package com.liy.today.views.action;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;


@TargetApi(Build.VERSION_CODES.KITKAT)
public class KitKatStatusBarPlaceholder extends View {

    /**
     * KitKatStatusBarPlaceholder
     * @param context context
     */
    public KitKatStatusBarPlaceholder(Context context) {
        super(context);
        init();
    }

    /**
     * KitKatStatusBarPlaceholder
     * @param context context
     * @param attrs attrs
     */
    public KitKatStatusBarPlaceholder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * KitKatStatusBarPlaceholder
     * @param context context
     * @param attrs attrs
     * @param defStyleAttr defStyleAttr
     */
    public KitKatStatusBarPlaceholder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setFitsSystemWindows(true);
        }

        //>=5.0 进入这个逻辑，否则进入fitSystemWindows(Rect)逻辑
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
                @Override
                public android.view.WindowInsets onApplyWindowInsets(View v, android.view.WindowInsets insets) {
                    setContentBottomPadding(insets.getSystemWindowInsetBottom());
                    return KitKatStatusBarPlaceholder.this.onApplyWindowInsets(insets);
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int windowFlag = ((Activity)getContext()).getWindow().getAttributes().flags;
            if( (windowFlag & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0) {
                height = getStatusBarHeight();
            }
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //避免沉浸式状态栏后输入法不能Resize布局
    //http://stackoverflow.com/questions/21092888/windowsoftinputmode-adjustresize-not-working-with-translucent-action-navbar
    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected final boolean fitSystemWindows(@NonNull Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setContentBottomPadding(insets.bottom);
        }
        return super.fitSystemWindows(insets);
    }
    
    private void setContentBottomPadding(int bottomPadding) {
        if(getContext() instanceof Activity) {
            View contentView = ((Activity) getContext()).findViewById(android.R.id.content);
            contentView.setPadding(contentView.getPaddingLeft(), contentView.getPaddingTop(),
                    contentView.getPaddingRight(), bottomPadding);
        }
    }
}
