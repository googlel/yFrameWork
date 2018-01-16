package com.liy.today.utils;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/10/25
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class ObjectUtils {

    /**
     * ObjectUtils
     */
    public ObjectUtils() {
        throw new AssertionError();
    }

    /**
     * compare two object
     *
     * @param actual actual
     * @param expected expected
     * @return <ul>
     *         <li>if both are null, return true</li>
     *         <li>return actual.{@link Object#equals(Object)}</li>
     *         </ul>
     */
    public static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    /**
     * compare two object
     * <ul>
     * <strong>About result</strong>
     * <li>if v1 > v2, return 1</li>
     * <li>if v1 = v2, return 0</li>
     * <li>if v1 < v2, return -1</li>
     * </ul>
     * <ul>
     * <strong>About rule</strong>
     * <li>if v1 is null, v2 is null, then return 0</li>
     * <li>if v1 is null, v2 is not null, then return -1</li>
     * <li>if v1 is not null, v2 is null, then return 1</li>
     * <li>return v1.{@link Comparable#compareTo(Object)}</li>
     * </ul>
     *
     * @param v1 v1
     * @param v2 v2
     * @return <V>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <V> int compare(V v1, V v2) {
        return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1 : ((Comparable)v1).compareTo(v2));
    }

    /**
     * get size of list
     *
     * <pre>
     * getSize(null)   =   0;
     * getSize({})     =   0;
     * getSize({1})    =   1;
     * </pre>
     *
     * @param <V> <V>
     * @param sourceList sourceList
     * @return if list is null or empty, return 0, else return {@link List#size()}.
     */
    public static <V> int getSize(List<V> sourceList) {
        return sourceList == null ? 0 : sourceList.size();
    }

    /**
     * is null or its size is 0
     *
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1})    =   false;
     * </pre>
     *
     * @param <V> <V>
     * @param sourceList sourceList
     * @return if list is null or its size is 0, return true, else return false.
     */
    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

    /**
     * invert list
     *
     * @param <V> <V>
     * @param sourceList sourceList
     * @return <V> List</V>
     */
    public static <V> List<V> invertList(List<V> sourceList) {
        if (isEmpty(sourceList)) {
            return sourceList;
        }

        List<V> invertList = new ArrayList<V>(sourceList.size());
        for (int i = sourceList.size() - 1; i >= 0; i--) {
            invertList.add(sourceList.get(i));
        }
        return invertList;
    }

    /**
     * 检查是否为空
     * @param object object
     * @param message message
     * @param <T> <T>
     * @return T
     */
    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * 检查是否是借口类型
     * @param service service
     * @param <T>  <T>
     */
    public static <T> void validateServiceInterface(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
    }


    /**
     * 是否List为空
     * @param list list
     * @return isListEmpty
     */
    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 从Map转对象
     * @param map map
     * @param c c
     * @param <T> <T>
     * @return <T>
     */
    public static <T> T fromMap(Map map, Class<T> c){
        return GsonUtils.fromJson(GsonUtils.toJson(map), c);
    }

    /**
     * 对象转Map
     * @param obj obj
     * @return Map
     */
    public static Map<String, Object> toMap(Object obj){
        return GsonUtils.fromJson(GsonUtils.toJson(obj)
                , new TypeToken<HashMap<String, Object>>(){}.getType());
    }

    /**
     * 合并两个Map对象
     * @param origin origin
     * @param mergeIn mergeIn
     */
    public static void mergeMap(Map<String, Object> origin, Map<String, Object> mergeIn){
        for(Map.Entry<String, Object> entry : mergeIn.entrySet()){
            String entryKey = entry.getKey();
            Object entryValue = entry.getValue();
            if(entryValue instanceof Map){
                Object originValue = origin.get(entryKey);
                if(originValue instanceof Map){
                    mergeMap((Map)originValue, (Map)entryValue);
                    continue;
                }
            }
            if(entryValue!=null){
                origin.put(entryKey, entryValue);
            }
        }
    }
}
