package com.hmily.cloud.dubbo.feign.samples.consumer;

import com.hmily.cloud.dubbo.feign.core.DubboFeignScan;
import com.hmily.cloud.dubbo.feign.core.EnableDubboFeignClients;
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
@ImportResource({
        "classpath*:dubbo/dubbo-demo*.xml"
})
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableDubboFeignClients
@DubboFeignScan("com.hmily.cloud.dubbo.feign.samples.consumer.integration")
public class ConsumerApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplicationRunner.class);
        System.out.println("【【【【【【【【 微服务 ConsumerApplicationRunner 已启动 】】】】】】】】");
    }
}
