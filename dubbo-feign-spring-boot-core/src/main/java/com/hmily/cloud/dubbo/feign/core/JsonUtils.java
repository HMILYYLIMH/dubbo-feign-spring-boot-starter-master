package com.hmily.cloud.dubbo.feign.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;

/**
 * <h1>Json工具类。</h1>
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-06
 */
public class JsonUtils {

    /**
     * <h2>将 obj 转换为 json 字符串。</h2>
     *
     * @param obj
     * @return
     */
    public static String toJSONString(Object obj){
        return new Gson().toJson(obj);
    }

    /**
     * <h2>将 json 字符串按照 type 类型转换为对象。</h2>
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String json, Class<T> type){
        return new Gson().fromJson(json, type);
    }

    /**
     * <h2>将 json 字符串按照 type 类型转换为对象。</h2>
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Collection<T> parseArray(String json, Class<T> type){
        return new Gson().fromJson(json, new TypeToken<Collection<T>>() { }.getType());
    }
}