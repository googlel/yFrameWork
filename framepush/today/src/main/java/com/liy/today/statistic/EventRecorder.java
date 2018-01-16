package com.liy.today.statistic;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.liy.today.APPEnvironment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import de.greenrobot.event.EventBus;


/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/12/3
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */

public class EventRecorder {

    private static boolean isAppInBackground = false;
    private static WeakHashMap<Activity, UBTPageEvent> mPageEventCache = new WeakHashMap<>();
    private static UBTPageEvent mAppEvent;
    private static Object mLifecycleCallbacks;

    /**
     * 在Application的onCreate中调用
     * @param context context
     */
    public static void initEventReport(Context context){
        if(mLifecycleCallbacks ==null){
            context.registerComponentCallbacks(new ComponentCallbacks2() {
                @Override
                public void onTrimMemory(int level) {
                    if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
                        flushEventApp(APPEnvironment.getPackageName(), EventKey.APP_END);
                        isAppInBackground = true;
                        if (mPageEventCache != null && !mPageEventCache.isEmpty()) {
                            Iterator iter = mPageEventCache.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry<Activity, UBTPageEvent> entry =
                                        (Map.Entry<Activity, UBTPageEvent>) iter.next();
                                if (entry == null) {
                                    continue;
                                }
                                EventBus.getDefault().post(entry.getValue());
                            }
                        }
                    }
                }

                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                }

                @Override
                public void onLowMemory() {
                }
            });

            Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                }
                @Override
                public void onActivityStarted(Activity activity) {
                }
                @Override
                public void onActivityResumed(Activity activity) {
                    if(isAppInBackground){
                        isAppInBackground = false;
                        flushEventApp(activity.getPackageName(), EventKey.APP_FRONT);
                    }
                    try {
                        onEventPageShow(activity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onActivityPaused(Activity activity) {
                    try {
                        flushEventWithEnd(activity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onActivityStopped(Activity activity) {
                }
                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }
                @Override
                public void onActivityDestroyed(Activity activity) {
                    flushEventWithEnd(activity);
                }
            };
            mLifecycleCallbacks = callbacks;
            Context app = context.getApplicationContext();
            if(app instanceof Application){
                ((Application) app).registerActivityLifecycleCallbacks(callbacks);
            }
        }
    }

    /**
     * 统计App启动/前台/后台的事件
     */
    private static void flushEventApp(@NonNull String packName, String eventId){
        if (mAppEvent == null) {
            mAppEvent = new UBTPageEvent();
            mAppEvent.setPageId(packName);
            mAppEvent.setPageName(APPEnvironment.getAppName());
            mAppEvent.setUbtData(new ArrayList<>());
        }
        mAppEvent.getUbtData().add(getEventActionInfo(eventId, eventId));
        EventBus.getDefault().post(mAppEvent);
    }

    /**
     * 记录页面展现
     * @param activity activity
     */
    public static void onEventPageShow(Activity activity){
        flushEvents(activity);
        UBTPageEvent pageEvent = mPageEventCache.get(activity);
        if(pageEvent == null){
            pageEvent = new UBTPageEvent();
        }

        if (fillPageInfo(activity, pageEvent)) {
            pageEvent.setUbtData(new ArrayList<>());
            mPageEventCache.put(activity, pageEvent);
            pageEvent.getUbtData().add(
                    getEventActionInfo(
                            pageEvent.getPageId()
                            , EventKey.PAGE_SHOW));
        }
    }

    /**
     * 发送数据
     * @param activity activity
     */
    private static void flushEventWithEnd(Activity activity){
        UBTPageEvent event = mPageEventCache.remove(activity);
        if(event != null) {
            event.getUbtData().add(getEventActionInfo(
                    event.getPageId(),
                    EventKey.PAGE_END));
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 发送数据
     * @param activity activity
     */
    private static void flushEvents(Activity activity){
        UBTPageEvent event = mPageEventCache.remove(activity);
        if(event != null) {
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 填充数据
     * @param activity activity
     * @param ubtPageEvent ubtPageEvent
     * @return 是否被填充
     */
    private static boolean fillPageInfo(Activity activity, UBTPageEvent ubtPageEvent){
        if(ubtPageEvent != null){
            Class<?> clz = activity.getClass();
            PageEvent annotation = clz.getAnnotation(PageEvent.class);
            if (annotation != null) {
                ubtPageEvent.setPageId(annotation.pageId());
                ubtPageEvent.setPageName(annotation.pageName());
                ubtPageEvent.setReferPageName(annotation.referPageName());
                ubtPageEvent.setReferPageId(annotation.referPageId());
                return true;
            }
        }
        return false;
    }

    /**
     * 获得单个数据
     * @param name name
     * @param action action
     * @return UBTActionEevnt
     */
    private static UBTActionEevnt getEventActionInfo(String name, String action) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(action)) {
            return null;
        }
        UBTActionEevnt actionEevnt = new UBTActionEevnt();
        actionEevnt.setName(name);
        actionEevnt.setAction(action);
        actionEevnt.setTimemills(String.valueOf(System.currentTimeMillis()));
        return actionEevnt;
    }

    private static void onEventAction(Context context, String name, String action) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(action)) {
            return;
        }

        if (mPageEventCache == null) {
            return;
        }

        UBTPageEvent ubtPageEvent = mPageEventCache.get(context);
        if (ubtPageEvent == null) {
            return;
        }
        ubtPageEvent.getUbtData().add(getEventActionInfo(name, action));
    }

    /**
     * addClickEvent
     * @param target target
     */
    public static void addClickEvent(Context context, String target) {
        onEventAction(context, target, "click");
    }

    /**
     * 添加其他类型事件
     * @param name name
     * @param action action
     */
    public static void addEvent(Context context, String name, String action) {
        onEventAction(context, name, action);
    }

}
