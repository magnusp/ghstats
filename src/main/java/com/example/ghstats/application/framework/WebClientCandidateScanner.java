package com.example.ghstats.application.framework;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WebClientCandidateScanner extends ClassPathScanningCandidateComponentProvider {

    private final SimpleBeanDefinitionRegistry bdr;
    private final ClassPathBeanDefinitionScanner bds;
    private final String basePackage;

    @Autowired
    public WebClientCandidateScanner(String basePackage) {
        super(false);
        this.basePackage = basePackage;

        addIncludeFilter(new AnnotationTypeFilter(RequestMapping.class, false));
        bdr = new SimpleBeanDefinitionRegistry();
        bds = new ClassPathBeanDefinitionScanner(bdr, false);
        bds.setIncludeAnnotationConfig(false);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        if(!beanDefinition.getMetadata().isInterface()) {
            return false;
        }

        Arrays.stream(bdr.getBeanDefinitionNames()).forEach(bdr::removeBeanDefinition);
        bds.resetFilters(false);

        var beanClassName = beanDefinition.getBeanClassName();
        try {
            final Class<?> aClass = Class.forName(beanClassName);
            bds.addIncludeFilter(new AssignableTypeFilter(aClass));
            bds.scan(basePackage);
            return bdr.getBeanDefinitionCount() == 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Class<?>> proxyCandidates() {
        return findCandidateComponents(basePackage).stream().map(beanDefinition -> {
            try {
                return Class.forName(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }
}
