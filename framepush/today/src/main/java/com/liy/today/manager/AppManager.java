package com.liy.today.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class AppManager {

    private Stack<Activity> activityStack;
    // 定义一个私有的静态全局变量来保存该类的唯一实例
    private static AppManager instance;

    // 构造函数必须是私有的 这样在外部便无法使用 new 来创建该类的实例
    private AppManager() {
        activityStack = new Stack<Activity> ();
    }

    /**
     * 单一实例
     */
    public synchronized static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager ();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add (activity);
    }

    /**
     * 从堆栈移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove (activity);
            activity = null;
        }
    }

    public Activity getTopActivity() {

        for (int i = 1; i < activityStack.size (); i++) {
            finishActivity (activityStack.get (i));

        }
        return activityStack.get (0);

    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement ();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        finishActivity (activityStack.lastElement ());
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass ().equals (cls)) {
                finishActivity (activity);
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove (activity);
            activity.finish ();
            activity = null;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size (); i < size; i++) {
            if (null != activityStack.get (i)) {
                activityStack.get (i).finish ();
            }
        }
        activityStack.clear ();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void onAppExit(Context context) {
        try {
            finishAllActivity ();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService (Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage (context.getPackageName ());
            System.exit (0);
        } catch (Exception e) {
        }
    }
}
