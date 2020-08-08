package com.hmily.cloud.dubbo.feign.core;

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h1>DubboClient 代理类。</h1>
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-01
 */
public class DubboClientProxy<T> implements InvocationHandler, Serializable {

    private ApplicationContext appCtx;
    private static final Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();
    private ClassLoader classLoader;

    public DubboClientProxy(ApplicationContext appCtx) {
        this.appCtx = appCtx;
        this.classLoader = appCtx.getClassLoader();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        if(Object.class.equals(declaringClass)){
            return method.invoke(this, args);
        } else if(isDefaultMethod(method)){
            return invokeDefaultMethod(proxy, method, args);
        } else if(!declaringClass.isAnnotationPresent(DubboFeignClient.class)){
            return method.invoke(this, args);
        }

        // 获取远端接口类
        DubboFeignClient dubboClientAnno = declaringClass.getAnnotation(DubboFeignClient.class);
        DubboMethod methodAnno = method.getDeclaredAnnotation(DubboMethod.class);
        Class<?> remoteClass = dubboClientAnno.remoteClass();
        String mtdName = getMethodName(method.getName(), methodAnno);

        // 缓存方法
        Method remoteMethod = cachedMethod(remoteClass, mtdName, methodAnno);
        Class<?> returnType = method.getReturnType();

        // 发起真正远程调用
        Object resultObject = doInvoke(remoteClass, remoteMethod, args, methodAnno);
        if (CharSequence.class.isAssignableFrom(returnType)) {
            return (String) resultObject;
        } else if (Collection.class.isAssignableFrom(returnType)) {
            return JsonUtils.parseArray(JsonUtils.toJSONString(resultObject), returnType);
        } else {
            Map resultMap = (Map) resultObject;
            return JsonUtils.parseObject(JsonUtils.toJSONString(resultMap), returnType);
        }
    }

    private String getMethodName(String mtdName, DubboMethod methodAnno) {
        if (methodAnno == null) {
            return mtdName;
        }

        String remoteMethodName = methodAnno.remoteMethodName();
        if (remoteMethodName == null || remoteMethodName.isEmpty()) {
            return mtdName;
        }

        return remoteMethodName;
    }

    private Method cachedMethod(Class<?> remoteClass, String mtdName, DubboMethod methodAnno) throws NoSuchMethodException, ClassNotFoundException {
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
                    classes[idx++] = classLoader.loadClass(name);
                }
                Method foundMethod = remoteClass.getDeclaredMethod(mtdName, classes);
                return METHOD_CACHE.computeIfAbsent(key, k -> foundMethod);
            }

            throw new DubboMethodException("One must be choosed in 'remoteMethodParamsTypeName' and " +
                    "'remoteMethodParamsTypeClass'.");
        }
    }

    private Method findMethodByName(String mtdName, Method[] remoteDeclaredMethods) {
        for (Method remoteMethod : remoteDeclaredMethods) {
            if(mtdName.equals(remoteMethod.getName())){
                return remoteMethod;
            }
        }
        return null;
    }

    private boolean isDefaultMethod(Method method) {
        return (method.getModifiers()
                & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
                && method.getDeclaringClass().isInterface();
    }

    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        return constructor
                .newInstance(declaringClass,
                        MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
                                | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }

    private Object doInvoke(Class<?> remoteClass, Method remoteMethod, Object[] args, DubboMethod methodAnno) throws InvocationTargetException, IllegalAccessException {
        Object remoteBean = getBean(remoteClass);

        Class<?>[] parameterTypes = remoteMethod.getParameterTypes();
        String[] parameterTypeList = new String[parameterTypes.length];
        Object[] parameterValueList = new Object[parameterTypes.length];
        int idx = 0;
        for (Class<?> parameterType : parameterTypes) {
            if(remoteBean == null){
                parameterTypeList[idx] = parameterType.getName();
                parameterValueList[idx] = JsonUtils.parseObject(JsonUtils.toJSONString(args[idx]), Map.class);
            } else {
                parameterValueList[idx] = JsonUtils.parseObject(JsonUtils.toJSONString(args[idx]), remoteMethod.getParameterTypes()[idx]);
            }
            idx++;
        }

        if(remoteBean != null){
            return remoteMethod.invoke(remoteBean, parameterValueList);
        }

        // RPC 远程调用
        GenericService genericService = getGenericService(remoteMethod, methodAnno);
        return genericService.$invoke(remoteMethod.getName(), parameterTypeList, parameterValueList);
    }

    private GenericService getGenericService(Method remoteMethod, DubboMethod methodAnno) {
        Class<?> declaringClass = remoteMethod.getDeclaringClass();

        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig();
        referenceConfig.setRegistry(getRegistry());
        referenceConfig.setApplication(getApplication());
        referenceConfig.setInterface(declaringClass.getName());
        referenceConfig.setGeneric(true);
        referenceConfig.setCheck(false);
        referenceConfig.setMethods(getMethodConfigList(remoteMethod, methodAnno));

        // 服务缓存处理
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(referenceConfig);
        if (genericService == null) {
            cache.destroy(referenceConfig);
            throw new DubboMethodException("Service is unavailable: " + declaringClass + "." + remoteMethod.getName());
        }

        return genericService;
    }

    private RegistryConfig getRegistry() {
        RegistryConfig config = new RegistryConfig();
        config.setAddress(ConfigUtils.getProperty("dubbo.registry.address"));
        return config;
    }

    private ApplicationConfig getApplication() {
        ApplicationConfig config = new ApplicationConfig();
        config.setName(ConfigUtils.getProperty("dubbo.application.name"));
        return config;
    }

    private List<? extends MethodConfig> getMethodConfigList(Method remoteMethod, DubboMethod methodAnno) {
        List<MethodConfig> mtdList = new ArrayList<>();

        MethodConfig cfg = new MethodConfig();
        mtdList.add(cfg);

        cfg.setName(remoteMethod.getName());
        cfg.setTimeout(getMethodTimeout(methodAnno));
        cfg.setRetries(getMethodRetries(methodAnno));

        return mtdList;
    }

    private Integer getMethodRetries(DubboMethod methodAnno) {
        if (methodAnno == null) {
            return DubboMethodConsts.DEFAULT_METHOD_RETRIES;
        }
        int retries = methodAnno.retries();
        if (retries < -1) {
            return DubboMethodConsts.DEFAULT_METHOD_RETRIES;
        }
        return retries;
    }

    private Integer getMethodTimeout(DubboMethod methodAnno) {
        if (methodAnno == null) {
            return DubboMethodConsts.DEFAULT_METHOD_TIMEOUT;
        }
        int timeout = methodAnno.timeout();
        if (timeout < 0) {
            return DubboMethodConsts.DEFAULT_METHOD_TIMEOUT;
        }
        return timeout;
    }

    private Object getBean(Class<?> remoteClass){
        try {
            return appCtx.getBean(remoteClass);
        } catch (Exception e) {
            return null;
        }
    }
}