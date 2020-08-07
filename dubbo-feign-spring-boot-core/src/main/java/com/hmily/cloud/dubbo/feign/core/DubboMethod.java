package com.hmily.cloud.dubbo.feign.core;

import java.lang.annotation.*;

/**
 * <h1>Dubbo 方法注解。</h1><br/>
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-07-31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DubboMethod {

    /**
     * 远端 dubbo 接口的方法名称。<br/>
     *
     * <li><h2>注意：如果 remoteMethodName 不赋值的话，那么程序会使用被 {@linkplain DubboFeignClient} 注解过的类中的方法名。</h2></li>
     *
     * @return
     */
    String remoteMethodName() default "";

    /**
     * 远端 dubbo 接口的方法参数类型全类名。<br/>
     *
     * <li><h2>注意：remoteMethodParamsTypeName 与 remoteMethodParamsTypeClass 只要填一个即可，不能两者都填写。</h2></li>
     *
     * @return
     */
    String[] remoteMethodParamsTypeName() default {};

    /**
     * 远端 dubbo 接口的方法参数类型Class。<br/>
     *
     * <li><h2>注意：remoteMethodParamsTypeName 与 remoteMethodParamsTypeClass 只要填一个即可，不能两者都填写。</h2></li>
     *
     * @return
     */
    Class<?>[] remoteMethodParamsTypeClass() default {};
}