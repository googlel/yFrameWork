package com.liy.today.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/11/9
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class GsonUtils {

    private static Gson mGson = new GsonBuilder().disableHtmlEscaping().create();

    public static Gson getGson() {
        return mGson;
    }

    /**
     * fromJson
     *
     * @param json json
     * @param c    c
     * @param <T>  <T>
     * @return T
     */
    public static <T> T fromJson(String json, Class<T> c) {
        return mGson.fromJson(json, c);
    }

    /**
     * fromJson
     *
     * @param json json
     * @param c    c
     * @param <T>  <T>
     * @return T
     */
    public static <T> T fromJson(JsonElement json, Class<T> c) {
        return mGson.fromJson(json, c);
    }

    /**
     * fromJson
     *
     * @param json json
     * @param type type
     * @param <T>  <T>
     * @return T
     */
    public static <T> T fromJson(JsonElement json, Type type) {
        return mGson.fromJson(json, type);
    }

    /**
     * fromJson
     *
     * @param json json
     * @param type type
     * @param <T>  <T>
     * @return T
     */
    public static <T> T fromJson(String json, Type type) {
        return mGson.fromJson(json, type);
    }

    /**
     * toJson
     *
     * @param src src
     * @return String
     */
    public static String toJson(Object src) {
        return mGson.toJson(src);
    }
}
