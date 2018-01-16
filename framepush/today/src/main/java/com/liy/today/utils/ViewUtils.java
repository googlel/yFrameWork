package com.liy.today.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/10/25
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class ViewUtils {

    private static DisplayMetrics sDisplayMetrics;

    /**
     * 初始化操作
     *
     * @param context context
     */
    public static void init(Context context) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    /**
     * 获取屏幕宽度 单位：像素
     *
     * @return 屏幕宽度
     */
    public static int getWidthPixels() {
        return sDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度 单位：像素
     *
     * @return 屏幕高度
     */
    public static int getHeightPixels() {
        return sDisplayMetrics.heightPixels;
    }

    /**
     * sp2Px
     * @param context context
     * @param sp sp
     * @return float
     */
    public static float sp2Px(Context context, int sp) {
      return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
              context.getResources().getDisplayMetrics());
    }

    /**
     * dp2Px
     * @param context context
     * @param dp dp
     * @return float
     */
    public static float dp2Px(Context context, int dp) {
      return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
              context.getResources().getDisplayMetrics());
    }

    /**
     * px2Dp
     * @param context context
     * @param px px
     * @return float
     */
    public static float px2Dp(Context context, int px) {
      return context.getResources().getDisplayMetrics().density * px;
    }
    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }
}
