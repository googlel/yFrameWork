package com.liy.today.cache;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.liy.today.utils.GsonUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.internal.Util;
import okio.Buffer;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class CacheManager {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Context mContext;
    private final VolleyCache volleyCache;
    private final MemoryCache memoryCache;
    private static CacheManager mCacheManager;

    /**
     * CacheManager
     * @param context context
     * @return
     */
    public static CacheManager init(Context context) {
        mCacheManager = new CacheManager(context);
        return mCacheManager;
    }

    /**
     * CacheManager
     * @param context context
     */
    private CacheManager(Context context) {
        volleyCache = VolleyCache.get(context);
        memoryCache = new MemoryCache(128);
        mContext = context;
    }

    /**
     * getCache
     * @return CacheManager
     */
    public static CacheManager getCache() {
        return mCacheManager;
    }

    /**
     * clearCache
     */
    public void clearCache() {
        volleyCache.clear();
        clearExternalCache();
    }

    /**
     * clearExternalCache
     */
    public void clearExternalCache() {
        File cacheDir = mContext.getExternalCacheDir();
        if (cacheDir != null) {
            deleteFile(cacheDir.listFiles());
        }
    }

    /**
     * deleteFile
     * @param files files
     */
    public static void deleteFile(File[] files) {
        if (files == null) return;
        File fileWillDelete = null;
        for (File f : files) {
            if (f.isDirectory()) {
                deleteFile(f.listFiles());
            }
            if (fileWillDelete == null) {
                fileWillDelete = new File(f.getParentFile(), ".delete");
            }
            f.renameTo(fileWillDelete);
            fileWillDelete.delete();
        }
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key        保存的key
     * @param value      保存的String数据
     * @param saveTime   保存的时间，单位：秒,如果<0，则不会过期
     * @param isInMemory 是不是存在内存中
     */
    private void put(String key, String value, int saveTime, boolean isInMemory) {
        if (saveTime == 0) return;
        if (isInMemory) {
            if (saveTime < 0) {
                memoryCache.put(key, value);
            } else {
                memoryCache.put(key, value, saveTime);
            }
        } else {
            if (saveTime < 0) {
                volleyCache.put(key, value);
            } else {
                volleyCache.put(key, value, saveTime);
            }
        }
    }

    /**
     * getAsString
     * @param key key
     * @return
     */
    private String getAsString(String key) {
        return getAsString(key, false);
    }

    /**
     * getAsString
     * @param key key
     * @param findInMemory findInMemory
     * @return
     */
    private String getAsString(String key, boolean findInMemory) {
        if (key == null) {
            return null;
        }
        if (findInMemory) {
            return memoryCache.getAsString(key);
        }
        return volleyCache.getAsString(key);
    }

    /**
     * 把请求结果缓存
     *
     * @param request  请求对象
     * @param response 请求结果
     */
    public void cacheResponse(Request request, String response) {
        if (request == null || response == null) {
            return;
        }

        CacheControl control = request.cacheControl();
        if (control == null) {
            return;
        }

        String method = request.method();
        if (TextUtils.isEmpty(method)) {
            return;
        }

        String body = "";
        if (request.body() != null && method.equals("POST")) {
            Buffer buffer = new Buffer();
            try {
                request.body().writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            body = buffer.readString(UTF8);
        }

        String urlCacheKey = Util.md5Hex(request.url() + body);

        // 如果 <= 0，则不会过期
        int maxAgeTime = control.maxAgeSeconds();
        maxAgeTime = maxAgeTime == 0 ? -1 : maxAgeTime;


        put(urlCacheKey, response, maxAgeTime, false);
    }

    /**
     * 拿出缓存请求结果
     * @param request 请求对象
     * @param clz 请求结果
     * @param <T> <T>
     * @return
     */
    public <T>T getCacheResponse(Request request, Class<T> clz) {
        return getCacheResponse(request, (Type) clz);
    }

    /**
     * 拿出缓存请求结果
     * @param request 请求对象
     * @param type type
     * @param <T> <T>
     * @return
     */
    public <T>T getCacheResponse(Request request, Type type) {
        if (request == null) {
            return null;
        }

        String method = request.method();
        if (TextUtils.isEmpty(method)) {
            method = "";
        }

        String body = "";
        if (request.body() != null && method.equals("POST")) {
            Buffer buffer = new Buffer();
            try {
                request.body().writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            body = buffer.readString(UTF8);
        }

        String urlCacheKey = Util.md5Hex(request.url() + body);

        String responseStr = getAsString(urlCacheKey);
        if (TextUtils.isEmpty(responseStr)) {
            return null;
        }

        TypeAdapter<?> adapter = GsonUtils.getGson().getAdapter(TypeToken.get(type));
        T result = null;
        try {
            result = (T) adapter.fromJson(responseStr);
        } catch (IOException | ClassCastException ex) {
            ex.printStackTrace();
        }
        return result;
    }


}
