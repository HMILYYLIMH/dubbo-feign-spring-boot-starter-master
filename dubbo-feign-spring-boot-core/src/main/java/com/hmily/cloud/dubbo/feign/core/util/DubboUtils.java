package com.hmily.cloud.dubbo.feign.core.util;

/**
 * <h1>Dubbo相关工具类。</h1>
 *
 * @author hmilyylimh
 *         ^_^
 * @version 0.0.1
 *         ^_^
 * @date 2020-08-08
 *
 */
public class DubboUtils {

    private static final String ALIBABA_DUBBO_PROXY_PREFIX = "com.alibaba.dubbo.common.bytecode.proxy0";
    private static final String APACHE_DUBBO_PROXY_PREFIX = "org.apache.dubbo.common.bytecode.proxy";

    public static boolean isDubboProxyName(String name) {
        return name.startsWith(ALIBABA_DUBBO_PROXY_PREFIX) || name.startsWith(APACHE_DUBBO_PROXY_PREFIX);
    }

    public static boolean isDubboProxyName(Object proxy) {
        return isDubboProxyName(proxy.getClass().getName());
    }
}