package com.hmily.cloud.dubbo.feign.core;

import com.hmily.cloud.dubbo.feign.core.DubboMethod;
import com.hmily.cloud.dubbo.feign.core.excp.DubboResultJudgeException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h1>方法缓存类。</h1>
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-10
 */
public class MethodCache {

    private static final Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();

    static Method cachedMethod(Class<?> remoteClass, String mtdName, DubboMethod methodAnno) throws NoSuchMethodException, ClassNotFoundException {
        String key = String.join("_", remoteClass.getName(), mtdName);
        if(methodAnno == null){
            return METHOD_CACHE.computeIfAbsent(key, k -> findMethodByName(mtdName, remoteClass.getDeclaredMethods()));
        }

        String[] remoteMethodParamsTypeName = methodAnno.remoteMethodParamsTypeName();
        Class<?>[] remoteMethodParamsTypeClass = methodAnno.remoteMethodParamsTypeClass();
        int nameLength = remoteMethodParamsTypeName.length;
        int classLength = remoteMethodParamsTypeClass.length;
        if (nameLength == 0) {
            if (classLength == 0) {
                return METHOD_CACHE.computeIfAbsent(key, k -> findMethodByName(mtdName, remoteClass.getDeclaredMethods()));
            }

            Method method = METHOD_CACHE.get(key);
            if (method != null) {
                return method;
            }
            Method foundMethod = remoteClass.getDeclaredMethod(mtdName, remoteMethodParamsTypeClass);
            return METHOD_CACHE.computeIfAbsent(key, k -> foundMethod);
        } else {
            if (classLength == 0) {
                Method method = METHOD_CACHE.get(key);
                if (method != null) {
                    return method;
                }

                Class<?>[] classes = new Class<?>[nameLength];
                int idx = 0;
                for (String name : remoteMethodParamsTypeName) {
                    classes[idx++] = remoteClass.getClassLoader().loadClass(name);
                }
                Method foundMethod = remoteClass.getDeclaredMethod(mtdName, classes);
                return METHOD_CACHE.computeIfAbsent(key, k -> foundMethod);
            }

            throw new DubboResultJudgeException("One must be choosed in 'remoteMethodParamsTypeName' and " +
                    "'remoteMethodParamsTypeClass'.");
        }
    }

    private static Method findMethodByName(String mtdName, Method[] remoteDeclaredMethods) {
        for (Method remoteMethod : remoteDeclaredMethods) {
            if(mtdName.equals(remoteMethod.getName())){
                return remoteMethod;
            }
        }
        return null;
    }
}
