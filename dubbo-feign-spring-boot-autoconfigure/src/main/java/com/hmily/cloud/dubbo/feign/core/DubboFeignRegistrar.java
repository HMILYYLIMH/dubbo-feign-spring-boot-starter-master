package com.hmily.cloud.dubbo.feign.core;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 请输入一句美丽的描述话语
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-01
 */
public class DubboFeignRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        List<String> basePackages = parseBasePackages(importingClassMetadata);

        DubboFeignScanner scanner = new DubboFeignScanner(registry);

        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

    private List<String> parseBasePackages(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes annoAttrs =
                AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DubboFeignScan.class.getName()));

        List<String> basePackages = new ArrayList<>(8);
        for (String pkg : annoAttrs.getStringArray("value")) {
            if(StringUtils.hasText(pkg)){
                basePackages.add(pkg);
            }
        }

        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if(StringUtils.hasText(pkg)){
                basePackages.add(pkg);
            }
        }

        return basePackages;
    }
}
