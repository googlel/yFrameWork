package com.liy.today.cache;

import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class VolleyCache {
    private static final int TIME_HOUR = 60 * 60;
    private static final int TIME_DAY = TIME_HOUR * 24;
    private static final int MAX_CACHE_TIME = TIME_DAY * 30;
    private static final int MAX_SIZE = 1000 * 1000 * 5; // 5 mb
    private static final int MAX_COUNT = Integer.MAX_VALUE;
    private static final String DEFAULT_DIR_NAME = "VolleyCache";
    private static Map<String, VolleyCache> mInstanceMap = new HashMap<String, VolleyCache>();
    private DiskBasedCache mCache;

    public static VolleyCache get(Context ctx) {
        return get(ctx, DEFAULT_DIR_NAME);
    }

    public static VolleyCache get(Context ctx, String cacheName) {
        File f = new File(ctx.getExternalCacheDir(), cacheName);
        return get(f, MAX_SIZE, MAX_COUNT);
    }

    public static VolleyCache get(File cacheDir) {
        return get(cacheDir, MAX_SIZE, MAX_COUNT);
    }

    public static VolleyCache get(Context ctx, int max_size, int max_count) {
        File f = new File(ctx.getExternalCacheDir(), DEFAULT_DIR_NAME);
        return get(f, max_size, max_count);
    }

    public static VolleyCache get(File cacheDir, int max_size, int max_count) {
        VolleyCache manager = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
        if (manager == null) {
            manager = new VolleyCache(cacheDir, max_size, max_count);
            mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager);
        }
        return manager;
    }

    private static String myPid() {
        return "_" + android.os.Process.myPid();
    }

    private VolleyCache(File cacheDir, int max_size, int max_count) {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
        }
        mCache = new DiskBasedCache(cacheDir, max_size);
        //异步初始化
        new Thread(){
            @Override
            public void run() {
                super.run();
                mCache.initialize();
            }
        }.start();
    }

    // =======================================
    // ============ String数据 读写 ==============
    // =======================================
    /**
     * 保存 String数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的String数据
     */
    public void put(String key, String value) {
        put(key, value, MAX_CACHE_TIME);
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key 保存的key
     * @param value 保存的String数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, String value, int saveTime) {
        put(key, value.getBytes(), saveTime);
    }

    /**
     * 读取 String数据
     * @return String 数据
     */
    public String getAsString(String key) {
        byte[] data = getAsBinary(key);
        if(data==null) return null;
        return new String(data);
    }

    // =======================================
    // ============= JSONObject 数据 读写 ==============
    // =======================================
    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的JSON数据
     */
    public void put(String key, JSONObject value) {
        put(key, value.toString());
    }

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的JSONObject数据
     * @param saveTime
     *            保存的时间，单位：秒
     */
    public void put(String key, JSONObject value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    /**
     * 读取JSONObject数据
     * @return JSONObject数据
     */
    public JSONObject getAsJSONObject(String key) {
        String JSONString = getAsString(key);
        try {
            return new JSONObject(JSONString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============ JSONArray 数据 读写 =============
    // =======================================
    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的JSONArray数据
     */
    public void put(String key, JSONArray value) {
        put(key, value.toString());
    }

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的JSONArray数据
     * @param saveTime
     *            保存的时间，单位：秒
     */
    public void put(String key, JSONArray value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    /**
     * 读取JSONArray数据
     * @return JSONArray数据
     */
    public JSONArray getAsJSONArray(String key) {
        String JSONString = getAsString(key);
        try {
            return new JSONArray(JSONString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================
    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的数据
     */
    public void put(String key, byte[] value) {
        put(key, value, MAX_CACHE_TIME);
    }

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的数据
     * @param saveTime
     *            保存的时间，单位：秒
     */
    public void put(String key, byte[] value, int saveTime) {
        Cache.Entry entry = new Cache.Entry();
        entry.data = value;
        entry.ttl = System.currentTimeMillis() + saveTime;
        mCache.put(key, entry);
    }

    /**
     * 获取 byte 数据
     * @return byte 数据
     */
    @Nullable
    public byte[] getAsBinary(String key) {
        Cache.Entry entry = mCache.get(key);
        if(entry==null) return null;
        if(entry.isExpired()){
            mCache.remove(key);
            return null;
        }else{
            return entry.data;
        }
    }

    // =======================================
    // ============= 序列化 数据 读写 ===============
    // =======================================
    /**
     * 保存 Serializable数据 到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的value
     */
    public void put(String key, Serializable value) {
        put(key, value, -1);
    }

    /**
     * 保存 Serializable数据到 缓存中
     *
     * @param key
     *            保存的key
     * @param value
     *            保存的value
     * @param saveTime
     *            保存的时间，单位：秒
     */
    public void put(String key, Serializable value, int saveTime) {
        ByteArrayOutputStream baos;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (saveTime != -1) {
                put(key, data, saveTime);
            } else {
                put(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(oos!=null) oos.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     * 读取 Serializable数据
     * @return Serializable 数据
     */
    public Object getAsObject(String key) {
        byte[] data = getAsBinary(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bais != null)
                        bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null)
                        ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    /**
     * 移除某个key
     */
    public void remove(String key) {
        mCache.remove(key);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        mCache.clear();
    }

}