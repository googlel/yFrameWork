package com.liy.today.cache;

import android.support.v4.util.LruCache;

import java.util.Map;

/**
 * The Creator is Leone90 && E-mail: butleone@163.com
 *
 * @author Leone90
 * @date 15/12/14
 * Edit it! Change it! Beat it! Whatever!
 */
public class MemoryCache {
    private final LruCache<String, Value> memoryCache;

    public MemoryCache(int count) {
        memoryCache = new LruCache<>(count);
    }

    private void checkMemoryCacheValid(){
        try {
            for(Map.Entry<String, Value> entry : memoryCache.snapshot().entrySet()){
                if(entry.getValue()==null || !entry.getValue().isValid()){
                    memoryCache.remove(entry.getKey());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String key, String value) {
        checkMemoryCacheValid();
        memoryCache.put(key, new Value(value));
    }

    public void put(String key, String value, int saveTime) {
        checkMemoryCacheValid();
        memoryCache.put(key, new Value(value, saveTime));
    }

    public String getAsString(String key) {
        checkMemoryCacheValid();
        Value value = memoryCache.remove(key);
        if(value!=null){
            if(value.isValid()){
                memoryCache.put(key, value);
                return value.getValue();
            }
        }
        return null;
    }

    class Value{
        String value;
        Long invalidTime;

        Value(String value) {
            this.value = value;
        }
        /**
         * @param saveTime 要保存的时间，单位：秒
         */
        Value(String value, int saveTime) {
            this.value = value;
            if(saveTime>0){
                this.invalidTime = saveTime * 1000 + System.currentTimeMillis();
            }
        }
        boolean isValid(){
            if(invalidTime==null) return true;
            return System.currentTimeMillis() < invalidTime;
        }

        public String getValue() {
            return value;
        }
    }
}
