package com.hmily.cloud.dubbo.feign.core;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 请输入一句美丽的描述话语
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
