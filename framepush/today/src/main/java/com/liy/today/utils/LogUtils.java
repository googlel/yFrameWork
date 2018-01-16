package com.liy.today.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/10/25
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public final class LogUtils {
    private static final String PREFIX = "laiyifen:-----";
    private static boolean mLogEnable = true;
    private static List<String> mLogs = new ArrayList<>();
    private static List<String> mHtmlLogs = new ArrayList<>();
    private static List<String> mWebErrorLogs = new ArrayList<>();
    private static List<String> mNetLogs = new ArrayList<>();

    public static final String WEB_TAG = "WebView: ";
    public static final String WEB_ERR_TAG = "WebViewErr:";
    public static final String REQ_TAG = "request: ";
    public static final String RES_TAG = "response: ";

    /**
     * getLogs
     *
     * @return getLogs
     */
    public static List<String> getLogs() {
        Collections.reverse(mLogs);
        return mLogs;
    }

    /**
     * getNetLogs
     *
     * @return getNetLogs
     */
    public static List<String> getHtmlLogs() {
        Collections.reverse(mHtmlLogs);
        return mHtmlLogs;
    }

    /**
     * getNetLogs
     *
     * @return getNetLogs
     */
    public static List<String> getNetLogs() {
        Collections.reverse(mNetLogs);
        return mNetLogs;
    }

    /**
     * 设置Log开关
     *
     * @param enable 开关项(默认为开).
     */
    public static void setEnable(boolean enable) {
        mLogEnable = enable;
    }

    /**
     * log for debug
     *
     * @param message log message
     * @param tag     tag
     * @see Log#d(String, String)
     */
    public static void d(String tag, String message) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.d(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for debug
     *
     * @param obj log message
     * @param tag tag
     * @see Log#d(String, String)
     */
    public static void d(String tag, Object obj) {
        if (mLogEnable) {
            String msg = PREFIX + GsonUtils.toJson(obj);
            Log.d(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for debug
     *
     * @param message   log message
     * @param throwable throwable
     * @param tag       tag
     * @see Log#d(String, String, Throwable)
     */
    public static void d(String tag, String message, Throwable throwable) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.d(tag, msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
        }
    }

    /**
     * log for debug
     *
     * @param tag    tag
     * @param format message format, such as "%d ..."
     * @param params message content params
     * @see Log#d(String, String)
     */
    public static void d(String tag, String format, Object... params) {
        if (mLogEnable) {
            String msg = String.format(PREFIX + format, params);
            Log.d(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for warning
     *
     * @param message log message
     * @param tag     tag
     * @see Log#w(String, String)
     */
    public static void w(String tag, String message) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.w(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for warning
     *
     * @param tag       tag
     * @param throwable throwable
     * @see Log#w(String, Throwable)
     */
    public static void w(String tag, Throwable throwable) {
        if (mLogEnable) {
            Log.w(tag, throwable);
            writeToFile(tag, Log.getStackTraceString(throwable));
        }
    }

    /**
     * log for warning
     *
     * @param message   log message
     * @param throwable throwable
     * @param tag       tag
     * @see Log#w(String, String, Throwable)
     */
    public static void w(String tag, String message, Throwable throwable) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.w(tag, msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
        }
    }

    /**
     * log for warning
     *
     * @param tag    tag
     * @param format message format, such as "%d ..."
     * @param params message content params
     * @see Log#w(String, String)
     */
    public static void w(String tag, String format, Object... params) {
        if (mLogEnable) {
            String msg = String.format(PREFIX + format, params);
            Log.w(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for error
     *
     * @param message message
     * @param tag     tag
     * @see Log#i(String, String)
     */
    public static void e(String tag, String message) {
        String msg = PREFIX + message;
        Log.e(tag, msg);
        writeToFile(tag, msg);
    }

    /**
     * log for error
     *
     * @param message   log message
     * @param throwable throwable
     * @param tag       tag
     * @see Log#i(String, String, Throwable)
     */
    public static void e(String tag, String message, Throwable throwable) {
        String msg = PREFIX + message;
        Log.e(tag, msg, throwable);
        writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
    }

    /**
     * log for error
     *
     * @param tag    tag
     * @param format message format, such as "%d ..."
     * @param params message content params
     * @see Log#e(String, String)
     */
    public static void e(String tag, String format, Object... params) {
        String msg = String.format(PREFIX + format, params);
        Log.e(tag, msg);
        writeToFile(tag, msg);
    }

    /**
     * log for information
     *
     * @param message message
     * @param tag     tag
     * @see Log#i(String, String)
     */
    public static void i(String tag, String message) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.i(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for information
     *
     * @param obj obj
     * @param tag tag
     * @see Log#i(String, String)
     */
    public static void i(String tag, Object obj) {
        if (mLogEnable) {
            String msg = PREFIX + GsonUtils.toJson(obj);
            Log.i(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for information
     *
     * @param message   log message
     * @param throwable throwable
     * @param tag       tag
     * @see Log#i(String, String, Throwable)
     */
    public static void i(String tag, String message, Throwable throwable) {
        if (mLogEnable) {
            String msg = message;
            Log.i(tag, PREFIX + msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
        }
    }

    /**
     * log for information
     *
     * @param tag    tag
     * @param format message format, such as "%d ..."
     * @param params message content params
     * @see Log#i(String, String)
     */
    public static void i(String tag, String format, Object... params) {
        if (mLogEnable) {
            String msg = String.format(PREFIX + format, params);
            Log.i(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for verbos
     *
     * @param message log message
     * @param tag     tag
     * @see Log#v(String, String)
     */
    public static void v(String tag, String message) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.v(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for verbose
     *
     * @param message   log message
     * @param throwable throwable
     * @param tag       tag
     * @see Log#v(String, String, Throwable)
     */
    public static void v(String tag, String message, Throwable throwable) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.v(tag, msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
        }
    }

    /**
     * log for verbose
     *
     * @param tag    tag
     * @param format message format, such as "%d ..."
     * @param params message content params
     * @see Log#v(String, String)
     */
    public static void v(String tag, String format, Object... params) {
        if (mLogEnable) {
            String msg = String.format(PREFIX + format, params);
            Log.v(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * 将相关Log写入日志文件中
     *
     * @param tag tag
     * @param msg msg
     */
    private static void writeToFile(String tag, String msg) {
        synchronized (LogUtils.class) {
            if (tag.equalsIgnoreCase(WEB_TAG)) {
                synchronized (mHtmlLogs) {
                    if (mHtmlLogs.size() > 200) {
                        mHtmlLogs.remove(0);
                    }
                    mHtmlLogs.add(msg);
                }
            } else if (tag.equalsIgnoreCase(REQ_TAG) || tag.equalsIgnoreCase(RES_TAG)) {
                synchronized (mNetLogs) {
                    if (mNetLogs.size() > 200) {
                        mNetLogs.remove(0);
                    }
                    mNetLogs.add(msg);
                }
            } else if (tag.equalsIgnoreCase(WEB_ERR_TAG)) {
                synchronized (mWebErrorLogs) {
                    if (mWebErrorLogs.size() > 200) {
                        mWebErrorLogs.remove(0);
                    }
                    mWebErrorLogs.add(msg);
                }
            }
            if (mLogs.size() > 400) {
                mLogs.remove(0);
            }
            mLogs.add(String.format("%s %s", tag, msg));
        }
    }
}
