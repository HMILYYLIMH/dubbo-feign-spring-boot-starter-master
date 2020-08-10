package com.hmily.cloud.dubbo.feign.core;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Set;

/**
 * <h1>DubboFeign 扫描器。</h1>
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-01
 */
public class DubboFeignScanner extends ClassPathBeanDefinitionScanner {

    private DubboClientFactoryBean<?> factoryBean = new DubboClientFactoryBean<>();

    public DubboFeignScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void registerFilters() {
        addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if(beanDefinitions == null || beanDefinitions.isEmpty()){
            return beanDefinitions;
        }

        processBeanDefinitions(beanDefinitions);
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition = null;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition)holder.getBeanDefinition();
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.setBeanClass(factoryBean.getClass());
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();

        if (!(metadata.isInterface() && metadata.isIndependent())) {
            return false;
        }
        Map<String, Object> attributes = metadata.getAnnotationAttributes(DubboFeignClient.class.getName());
        if (attributes == null) {
            return false;
        }
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(attributes);
        if (annoAttrs == null) {
            return false;
        }
        Object remoteClass = annoAttrs.get("remoteClass");
        if (remoteClass == null) {
            return false;
        }

        return true;
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        }

        return false;
    }
}