package com.example.ghstats.application.framework;

import com.example.ghstats.github.GithubResource;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

@Configuration
public class InvokingWebClientProxyRegistrar implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware {
    private final WebClientCandidateScanner webClientCandidateScanner;
    private ClassLoader classLoader;

    public InvokingWebClientProxyRegistrar() {
        webClientCandidateScanner = new WebClientCandidateScanner("com.example.ghstats");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        for (Class<?> clazz: webClientCandidateScanner.proxyCandidates()) {

            String beanName = ClassUtils.getShortNameAsProperty(clazz);

            final AbstractBeanDefinition beanDefinition1 = BeanDefinitionBuilder
                    .genericBeanDefinition(clazz)
                    .setLazyInit(true)
                    .addConstructorArgValue(classLoader)
                    .addConstructorArgValue(clazz)
                    .setFactoryMethodOnBean("createInvokingWebClient", "invokingWebClientBeanFactory")
                    .getBeanDefinition();
            registry.registerBeanDefinition(beanName, beanDefinition1);
        }
    }

}
