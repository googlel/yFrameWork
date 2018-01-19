package com.liy.today.cache;


import com.liy.today.utils.LogUtils;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
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
