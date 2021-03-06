package com.hmily.cloud.dubbo.feign.core.util;

import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * <h1>反射调用工具类。</h1>
 *
 * @author hmilyylimh
 *         ^_^
 * @version 0.0.1
 *         ^_^
 * @date 2020-08-08
 *
 */
public class InvokeUtils {

    private static final String CGLIB_METHOD_PROXY_REGEX = "CGLIB\\$%s\\$[a-zA-Z0-9_]+\\$Proxy";

    /**
     * 反射调用。
     *
     * @param proxy
     * @param remoteMethod
     * @param args
     * @return
     * @throws Throwable
     */
    public static Object invoke(Object proxy, Method remoteMethod, Object[] args) throws Throwable {
        Object handler = null;
        Object result = null;
        if (DubboUtils.isDubboProxy(proxy)) {
            handler = getHandler(proxy, proxy.getClass().getDeclaredField("handler"));
            result = ((InvocationHandler) handler).invoke(proxy, remoteMethod, args);
        } else if (AopUtils.isJdkDynamicProxy(proxy)) {
            handler = getHandler(proxy, proxy.getClass().getSuperclass().getDeclaredField("h"));
            result = ((java.lang.reflect.InvocationHandler) handler).invoke(proxy, remoteMethod, args);
        } else {
            handler = getHandler(proxy, proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0"));
            MethodProxy methodProxy = findMethodProxy(proxy, remoteMethod.getName());
            result = ((org.springframework.cglib.proxy.MethodInterceptor) handler).intercept(proxy, remoteMethod, args, methodProxy);
        }

        return result;
    }

    private static Object getHandler(Object proxy, Field h) throws IllegalAccessException {
        ReflectionUtils.makeAccessible(h);
        return h.get(proxy);
    }

    private static MethodProxy findMethodProxy(Object proxy, String methodName) throws IllegalAccessException {
        Field[] declaredFields = proxy.getClass().getDeclaredFields();
        Pattern pattern = Pattern.compile(String.format(CGLIB_METHOD_PROXY_REGEX, methodName));
        for (Field field : declaredFields) {
            if (pattern.matcher(field.getName()).find()) {
                ReflectionUtils.makeAccessible(field);
                return (MethodProxy) field.get(proxy);
            }
        }
        return null;
    }
}