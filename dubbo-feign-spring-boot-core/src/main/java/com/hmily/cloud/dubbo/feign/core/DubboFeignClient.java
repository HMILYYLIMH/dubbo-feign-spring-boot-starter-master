package com.hmily.cloud.dubbo.feign.core;

import java.lang.annotation.*;

/**
 * <h1>DubboFeignClient 注解类。</h1>
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
public @interface DubboFeignClient {

    /**
     * 远端 dubbo 接口类。
     *
     * @return
     */
    Class<?> remoteClass();

    /**
     * 是否需要结果判断处理, 如果 needResultJudge = false 则不会使用 resultJudge 里面的属性值。
     *
     * @return
     */
    boolean needResultJudge();

    /**
     * 远端结果判断对象属性。
     *
     * @return
     */
    ResultJudge resultJudge();

    /**
     * <h2>如果 needResultJudge = true 的话，则该接口中的所有属性需要赋值。</h2>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({})
    @interface ResultJudge {

        /**
         * 远端 dubbo 接口响应码的节点路径。
         *
         * @return
         */
        String remoteCodeNode() default "";

        /**
         * 远端 dubbo 接口响应码的成功值列表。
         *
         * @return
         */
        String[] remoteCodeSuccValueList() default {};

        /**
         * 远端 dubbo 接口响应描述的节点路径。
         *
         * @return
         */
        String remoteMsgNode() default "";
    }
}