package com.example.ghstats;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    @Autowired
    private GenericApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        var scanner = new RequestMappingInterfaceScanner();
        final Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("com.example");
        final Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(RequestMapping.class);
        for(BeanDefinition beanDefinition : candidateComponents) {
            var scanned = ((ScannedGenericBeanDefinition) beanDefinition);
            Class clazz = Class.forName(scanned.getBeanClassName());
            InvocationHandler ih = new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }
            };
            //https://stackoverflow.com/questions/39507736/dynamic-proxy-bean-with-autowiring-capability
            //https://opencredo.com/blogs/dynamic-proxies-java/
            //context.registerBean(clazz, () -> ih);

            int i = 0;
        }
    }
    static class RequestMappingInterfaceScanner extends ClassPathScanningCandidateComponentProvider {

        public RequestMappingInterfaceScanner() {
            super(false);

            addIncludeFilter(new AnnotationTypeFilter(RequestMapping.class, false));
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return beanDefinition.getMetadata().isInterface();
        }
    }
}
