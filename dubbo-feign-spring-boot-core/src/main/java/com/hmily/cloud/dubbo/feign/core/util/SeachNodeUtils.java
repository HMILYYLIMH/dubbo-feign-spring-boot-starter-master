package com.hmily.cloud.dubbo.feign.core.util;

import java.util.Map;

/**
 * <h1>节点查找工具类。</h1>
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-8-10
 */
public class SeachNodeUtils {

    /**
     * 查找结点对应的值。
     *
     * @param mapKey
     * @param targetMap
     * @return
     */
    public static Object getValue(String mapKey, Map targetMap) {
        int idx = mapKey.indexOf(".");
        if (idx == -1) {
            return targetMap.get(mapKey);
        }
        String curr = mapKey.substring(0, idx);
        if (!targetMap.containsKey(curr)) {
            return null;
        }
        Object val = targetMap.get(curr);
        if (val == null) {
            return null;
        }

        String next = mapKey.substring(idx + 1);
        if (val instanceof Map) {
            return getValue(next, (Map) val);
        }

        // 查找目标是List可以，但是不能过程中
        return null;
    }
}