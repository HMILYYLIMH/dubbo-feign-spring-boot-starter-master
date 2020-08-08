package com.hmily.cloud.dubbo.feign.core;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <h1>启用 DubboFeign 注解类。</h1>
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-07-31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DubboFeignRegistrar.class)
public @interface EnableDubboFeignClients {

}