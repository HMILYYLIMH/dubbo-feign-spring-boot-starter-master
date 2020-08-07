package com.hmily.cloud.dubbo.feign.samples.provider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

/**
 * 请输入一句美丽的描述话语
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-01
 */
@ImportResource({"classpath*:META-INF/spring/dubbo-demo*.xml"})
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableDubbo
public class ProviderApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplicationRunner.class);
        System.out.println("【【【【【【【【 微服务 ProviderApplicationRunner 已启动 】】】】】】】】");
    }
}
