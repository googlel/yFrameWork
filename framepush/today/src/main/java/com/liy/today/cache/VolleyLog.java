package com.liy.today.cache;


import com.liy.today.utils.LogUtils;

/**
 * The Creator is Leone90 && E-mail: butleone@163.com
 *
 * @author Leone90
 * @date 15/12/14
 * Edit it! Change it! Beat it! Whatever!
 */
public class VolleyLog {
    private static final String Tag = VolleyLog.class.getSimpleName();
    static final boolean DEBUG = true;

    public static void d(String s) {
        LogUtils.d(Tag, s);
    }

    public static void d(String format, Object... args) {
        LogUtils.d(Tag, format, args);
    }

    public static void e(String s) {
        LogUtils.e(Tag, s);
    }

    public static void e(String format, Object... args) {
        LogUtils.e(Tag, format, args);
    }

    public static void v(String s) {
        LogUtils.d(Tag, s);
    }

    public static void v(String format, Object... args) {
        LogUtils.d(Tag, format, args);
    }
}
