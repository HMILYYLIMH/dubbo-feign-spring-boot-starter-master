package com.hmily.cloud.dubbo.feign.core;

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.hmily.cloud.dubbo.feign.core.excp.DubboResultJudgeException;
import com.hmily.cloud.dubbo.feign.core.util.InvokeUtils;
import com.hmily.cloud.dubbo.feign.core.util.JsonUtils;
import com.hmily.cloud.dubbo.feign.core.util.SeachNodeUtils;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

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

    public DubboClientProxy(ApplicationContext appCtx) {
        this.appCtx = appCtx;
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
        Method remoteMethod = MethodCache.cachedMethod(remoteClass, mtdName, methodAnno);
        Class<?> returnType = method.getReturnType();

        // 发起真正远程调用
        Object resultObject = doInvoke(remoteClass, remoteMethod, args, methodAnno);

        // 解析返回结果
        return doParse(dubboClientAnno, returnType, resultObject);
    }

    private Object doParse(DubboFeignClient dubboClientAnno, Class<?> returnType, Object resultObject) {
        if (CharSequence.class.isAssignableFrom(returnType)) {
            return (String) resultObject;
        } else if (Collection.class.isAssignableFrom(returnType)) {
            return JsonUtils.parseArray(JsonUtils.toJSONString(resultObject), returnType);
        } else {
            // 这里处理的是Map对象
            if (containsResultJudge(dubboClientAnno)) {
                List<String> remoteCodeSuccValueList = getRemoteCodeSuccValue(dubboClientAnno);
                String remoteCodeNode = getRemoteCodeNode(dubboClientAnno);
                String remoteMsgNode = getRemoteMsgNode(dubboClientAnno);
                Object remodeCodeObject = SeachNodeUtils.getValue(remoteCodeNode, (Map) resultObject);
                String remodeCodeVal = remodeCodeObject == null ? null : String.valueOf(remodeCodeObject);
                String remodeMsgVal = (String) SeachNodeUtils.getValue(remoteMsgNode, (Map) resultObject);
                if (!remoteCodeSuccValueList.contains(remodeCodeVal)) {
                    throw new DubboResultJudgeException(remodeCodeVal, remodeMsgVal);
                }
            }

            return JsonUtils.parseObject(JsonUtils.toJSONString(resultObject), returnType);
        }
    }

    private boolean containsResultJudge(DubboFeignClient dubboClientAnno) {
        return dubboClientAnno.needResultJudge();
    }

    private String getRemoteCodeNode(DubboFeignClient dubboClientAnno) {
        return dubboClientAnno.resultJudge().remoteCodeNode();
    }

    private String getRemoteMsgNode(DubboFeignClient dubboClientAnno) {
        return dubboClientAnno.resultJudge().remoteMsgNode();
    }

    private List<String> getRemoteCodeSuccValue(DubboFeignClient dubboClientAnno) {
        return Arrays.asList(dubboClientAnno.resultJudge().remoteCodeSuccValueList());
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

    private Object doInvoke(Class<?> remoteClass, Method remoteMethod, Object[] args, DubboMethod methodAnno) throws Throwable {
        Object proxy = getBean(remoteClass);

        Class<?>[] parameterTypes = remoteMethod.getParameterTypes();
        String[] parameterTypeList = new String[parameterTypes.length];
        Object[] parameterValueList = new Object[parameterTypes.length];
        int idx = 0;
        for (Class<?> parameterType : parameterTypes) {
            if (proxy == null) {
                parameterTypeList[idx] = parameterType.getName();
                parameterValueList[idx] = JsonUtils.parseObject(JsonUtils.toJSONString(args[idx]), Map.class);
            } else {
                parameterValueList[idx] = JsonUtils.parseObject(JsonUtils.toJSONString(args[idx]),
                        remoteMethod.getParameterTypes()[idx]);
            }
            idx++;
        }

        if (proxy != null) {
            return InvokeUtils.invoke(proxy, remoteMethod, parameterValueList);
        }

        // 泛化远程调用
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
            throw new DubboResultJudgeException("Service is unavailable: " + declaringClass + "." + remoteMethod.getName());
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