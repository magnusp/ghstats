package com.example.ghstats.application.framework;

import org.reactivestreams.Publisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

public class InvokingWebClient<T> implements InvocationHandler {
    private final WebClientConfig webClientConfig;
    private WebClient webClient;

    public InvokingWebClient(WebClientConfig webClientConfig) {
        this(WebClient.builder().baseUrl(webClientConfig.getBaseUrl()).build(), webClientConfig);
    }

    public InvokingWebClient(WebClient webClient, WebClientConfig webClientConfig) {
        this.webClient = webClient;
        this.webClientConfig = webClientConfig;
    }

    @Override
    public Publisher<T> invoke(Object o, Method method, Object[] arguments) {
        if (arguments == null) {
            arguments = new Object[0];
        }
        Class returnType = method.getReturnType();
        ParameterizedTypeReference<Object> typeParameter = discoverTypeParameter(method);

        WebClient.RequestBodySpec requestBodySpec = createRequestBodySpec(method, arguments);
        final WebClient.ResponseSpec responseSpec = requestBodySpec.retrieve();
        if(Flux.class.isAssignableFrom(returnType)) {
            return (Flux<T>) responseSpec.bodyToFlux(typeParameter);
        } else if (Mono.class.isAssignableFrom(returnType)) {
            return (Mono<T>) responseSpec.bodyToMono(typeParameter);
        } else {
            throw new RuntimeException("Invalid returntype (neither Mono nor Flux)");
        }
    }

    private ParameterizedTypeReference<Object> discoverTypeParameter(Method method) {
        final ParameterizedType genericReturnType = (ParameterizedType)method.getGenericReturnType();
        final Type[] actualTypeArguments = genericReturnType.getActualTypeArguments();

        ParameterizedTypeReference<Object> typeParameter;
        switch (actualTypeArguments.length) {
            case 0:
                throw new RuntimeException("No type found");
            case 1:
                typeParameter = ParameterizedTypeReference.forType(actualTypeArguments[0]);
                break;
            default:
                throw new RuntimeException("Too many types");
        }
        return typeParameter;
    }

    private WebClient.RequestBodySpec createRequestBodySpec(Method method, Object[] arguments) {
        RequestMapping[] requestMappingPair = getPair(method);
        final String uri = getUri(requestMappingPair[0], requestMappingPair[1]);
        final HttpMethod httpMethod = getHttpMethod(requestMappingPair[1]);

        Class<?>[]     types       = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        HashMap<String, Object> uriMapping = new HashMap<>();
        for (int i = 0; i < types.length; i++) {
            Object value = arguments[i];
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof PathVariable) {
                    uriMapping.put(((PathVariable) annotation).value(), value);
                } else if(annotation instanceof RequestParam) {
                    throw new RuntimeException("Not implemented");
                }
            }
        }

        return webClient
                .method(httpMethod)
                .uri(uri, uriMapping);
    }

    private RequestMapping[] getPair(Method method) {
        return new RequestMapping[] {
                AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), RequestMapping.class),
                AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class)
        };
    }

    private String getUri(RequestMapping parentRequestMapping, RequestMapping requestMapping) {
        final String[] parentPath = parentRequestMapping.path();
        final String[] path = requestMapping.path();
        final String trimmedPath = StringUtils.trimLeadingCharacter(path[0], '/');
        return parentPath[0] + "/" + trimmedPath;
    }

    private HttpMethod getHttpMethod(RequestMapping requestMapping) {
        final RequestMethod[] methods = requestMapping.method();
        switch(methods.length) {
            case 0:
                throw new IllegalArgumentException("Missing HTTP method");
            case 1: break;
            default:
                throw new IllegalArgumentException("Too many methods for a client");
        }

        return HttpMethod.resolve(methods[0].name());
    }
}