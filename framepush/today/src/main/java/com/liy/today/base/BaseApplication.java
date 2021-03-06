package com.liy.today.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.io.File;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class BaseApplication extends MultiDexApplication {

    //    public RefWatcher mRefWatcher;
    private static BaseApplication mBaseApplication;



    @Override
    public void onCreate() {
        super.onCreate();
//        mRefWatcher = LeakCanary.install (this);
        mBaseApplication = this;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();

    }
//
//    public RefWatcher getRefWatcher() {
//        return mRefWatcher;
////    }

    public static BaseApplication getBaseApplication() {
        return mBaseApplication;
    }

    @Override
    public File getExternalCacheDir() {
        File dir = super.getExternalCacheDir();
        if (dir != null) {
            return dir;
        }
        return getCacheDir();
    }

//    public static String getRouterUrl(String tag) {
//        return SCHEME + "://" + tag;
//    }



}
