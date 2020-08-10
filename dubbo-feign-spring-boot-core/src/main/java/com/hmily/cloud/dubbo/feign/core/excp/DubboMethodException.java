package com.hmily.cloud.dubbo.feign.core.excp;

/**
 * <h1>Dubbo方法运行时异常。</h1>
 *
 * @author hmilyylimh
 *         ^_^
 * @version 0.0.1
 *         ^_^
 * @date 2020-08-08
 *
 */
public class DubboMethodException extends RuntimeException{

    public DubboMethodException(String message) {
        super(message);
    }

    public DubboMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}