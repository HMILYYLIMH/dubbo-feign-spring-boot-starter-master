package com.hmily.cloud.dubbo.feign.core;

import java.lang.annotation.*;

/**
 * <h1>DubboFeign 扫描注解类。</h1>
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
public @interface DubboFeignScan {

    String[] value() default {};

    String[] basePackages() default {};
}