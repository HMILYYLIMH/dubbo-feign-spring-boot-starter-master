package com.hmily.cloud.dubbo.feign.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

/**
 * <h1>DubboClient 工厂Bean。</h1>
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-01
 */
public class DubboClientFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private Class<T> dubboClientInterface;
    private ApplicationContext appCtx;

    public DubboClientFactoryBean() {
    }

    public DubboClientFactoryBean(Class<T> dubboClientInterface) {
        this.dubboClientInterface = dubboClientInterface;
    }

    @Override
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(dubboClientInterface.getClassLoader(),
                new Class[]{dubboClientInterface}, new DubboClientProxy<>(appCtx));
    }

    @Override
    public Class<?> getObjectType() {
        return dubboClientInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
    }
}