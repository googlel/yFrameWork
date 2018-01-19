package com.liy.today.network;

import android.content.Context;
import android.os.Environment;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.liy.today.base.BaseApplication;
import com.liy.today.utils.FileUtils;
import com.liy.today.utils.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.ByteString;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class HttpClient implements Interceptor {


    private static HttpClient instance = null;
    private ObjectCache mObjectCache;
    private OkHttpClient okHttpClient;


    private HttpClient() {
    }

    public static HttpClient getInstance(Context context) {
        synchronized (HttpClient.class) {
            if (instance == null) {

                instance = new HttpClient(context);
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * HttpClient
     *
     * @param context context
     */
    private HttpClient(Context context) {
        File cacheDir = new File(context.getExternalCacheDir(), "http");
        Cache cache = new Cache(cacheDir, 30 * 1024 * 1024);

        String innerCachePath = context.getCacheDir().getAbsolutePath();
        copyExternalCacheInside(context, innerCachePath);
        mObjectCache = ObjectCache.open(0.05f, innerCachePath);
//        setCookieHandler(new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL));


        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)

                .addNetworkInterceptor(new StethoInterceptor())
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(cache)
                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)))
                .addInterceptor(this)
                .build();


    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 获取缓存路径
     *
     * @param context context
     * @return 存储路径
     */
    private String getCachePath(Context context) {
        File file = null;
        if (isExternalStorageWritable()) {
            file = getExternalCacheDir(context);
        }
        return file != null ? file.getAbsolutePath() : context.getCacheDir().getAbsolutePath();
    }

    /**
     * 获取外部目录缓存路径
     *
     * @param context context
     * @return 外部存储换成路径
     */
    private File getExternalCacheDir(Context context) {
        File file = BaseApplication.getBaseApplication().getExternalCacheDir();
        if (file == null) {
            final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
            file = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        }

        file.mkdirs();

        if (file.isDirectory()) {
            return file;
        }

        return null;
    }

    /**
     * 将缓存到外部存储空间的内容复制到data/data/.../cache目录下
     */
    private void copyExternalCacheInside(Context context, String innerCachePath) {
        if (isExternalStorageWritable()) {
            String path = getCachePath(context) + "/object";
            if (path.startsWith(innerCachePath)) {
                return;
            }
            File cacheFile = FileUtils.createFolder(path);
            if (cacheFile == null) {
                return;
            }
            File[] files = cacheFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                String fileStr = files[i].getName();
                FileUtils.copy(innerCachePath + File.separator + fileStr, files[i].getAbsolutePath());
            }
            FileUtils.delete(cacheFile);
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        HttpUrl.Builder urlBuilder = oldRequest.url().newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());

        Request.Builder requestBuilder = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(urlBuilder.build());
        requestBuilder.addHeader("X-Requested-With", "XMLHttpRequest");
        requestBuilder.addHeader("deviceId", SystemUtils.getUUid());


//        if (!TextUtils.isEmpty(APPEnvironment.getGpsCoordinate())) {
//            requestBuilder.addHeader("gpsCoordinate", APPEnvironment.getGpsCoordinate());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getGpsCityId())) {
//            requestBuilder.addHeader("gpsCityId", APPEnvironment.getGpsCityId());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getGpsApiType())) {
//            requestBuilder.addHeader("gpsApiType", APPEnvironment.getGpsApiType());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getPushToken())) {
//            requestBuilder.addHeader("pushToken", APPEnvironment.getPushToken());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getPushType())) {
//            requestBuilder.addHeader("pushType", APPEnvironment.getPushType());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getUserToken())) {
//            requestBuilder.addHeader("userToken", APPEnvironment.getUserToken());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getPackageName())) {
//            requestBuilder.addHeader("appType", APPEnvironment.getPackageName());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getAppVersion())) {
//            requestBuilder.addHeader("appVersion", APPEnvironment.getAppVersion());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getAppVersionCode())) {
//            requestBuilder.addHeader("appVersionCode", APPEnvironment.getAppVersionCode());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getAppChannel())) {
//            requestBuilder.addHeader("appChannel", APPEnvironment.getAppChannel());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getDeviceType())) {
//            requestBuilder.addHeader("deviceType", APPEnvironment.getDeviceType());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getDeviceOsVersion())) {
//            requestBuilder.addHeader("deviceOSVersion", APPEnvironment.getDeviceOsVersion());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getDeviceImei())) {
//            requestBuilder.addHeader("deviceIMEI", APPEnvironment.getDeviceImei());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getDeviceCpu())) {
//            requestBuilder.addHeader("deviceCPU", APPEnvironment.getDeviceCpu());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getDeviceNetType())) {
//            requestBuilder.addHeader("deviceNetType", APPEnvironment.getDeviceNetType());
//        }
//        if (!TextUtils.isEmpty(APPEnvironment.getResolution())) {
//            requestBuilder.addHeader("deviceResolution", APPEnvironment.getResolution());
//        }

        Request newRequest = requestBuilder.build();

//        LogUtils.d(LogUtils.NET_TAG, "Request Header: %s", newRequest.headers().toString());
//        LogUtils.d(LogUtils.NET_TAG, "Request Url: %s", urlBuilder.build().url().toString());

        Response response = null;
        try {
            response = chain.proceed(newRequest);
        } catch (SocketTimeoutException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    private static String urlToKey(Request request, Object body) {
        String requestMethod = request.method();
        String cacheKey = requestMethod.equals("POST")
                ? new StringBuffer(request.toString())
                .append(body != null ? body : "")
                .toString()
                : request.toString();
        return md5Hex(cacheKey);
    }

    public static String md5Hex(String s) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5bytes = messageDigest.digest(s.getBytes("UTF-8"));
            return ByteString.of(md5bytes).hex();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * close缓存,应用程序退出前调用
     */
    public void close() {
        if (mObjectCache != null) {
            mObjectCache.close();
        }
    }

    /**
     * get Object cache
     *
     * @return cache
     */
    public ObjectCache getObjectCache() {
        return mObjectCache;
    }

}
