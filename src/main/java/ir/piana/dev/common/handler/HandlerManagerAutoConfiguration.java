package ir.piana.dev.common.handler;

import ir.piana.dev.common.util.FinalContainer;
import ir.piana.dev.common.util.SelfExpiringHashMap;
import ir.piana.dev.common.util.SelfExpiringMap;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class HandlerManagerAutoConfiguration {
    @Bean("reactiveCommonThreadPool")
    public ExecutorService reactiveCommonThreadPool(@Autowired ReactiveCore reactiveCore) {
        return Executors.newFixedThreadPool(reactiveCore.threadPoolSize);
    }

    @Bean
    HandlerManager getHandlerManager(
            ApplicationContext applicationContext,
            @Qualifier("reactiveCommonThreadPool") ExecutorService executorService) throws ClassNotFoundException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Handler.class);

        Map<Class<?>, HandlerContainer> handlerContainerMap = new LinkedHashMap<>();
        for (String k : beansWithAnnotation.keySet()) {
            Map<Integer, Method> methodMap = new LinkedHashMap<>();
            Object bean = beansWithAnnotation.get(k);
            Class originalClass = null;
            if (bean.getClass().getName().contains("$$"))
                originalClass = Class.forName(bean.getClass().getGenericSuperclass().getTypeName());
            else
                originalClass = Class.forName(bean.getClass().getTypeName());

            Method[] originalDeclaredMethods = originalClass.getDeclaredMethods();
            Method[] beanDeclaredMethods = bean.getClass().getDeclaredMethods();
            for (Method originalMethod : originalDeclaredMethods) {
                ChainStep annotation = originalMethod.getAnnotation(ChainStep.class);
                if (annotation != null && originalMethod.getParameterCount() == 1 &&
                        originalMethod.getParameterTypes()[0].isAssignableFrom(HandlerContext.class) &&
                        (originalMethod.getReturnType().isAssignableFrom(void.class) ||
                                originalMethod.getReturnType().isAssignableFrom(CompletableFuture.class))) {
                    if (methodMap.containsKey(annotation.order()))
                        throw new RuntimeException("ChainStep by same order : " + annotation.order() + " on " + k);
                    for (Method beanMethod : beanDeclaredMethods) {
                        if (beanMethod.getName().equals(originalMethod.getName()) &&
                                beanMethod.getReturnType().equals(originalMethod.getReturnType()) &&
                                beanMethod.getParameterCount() == originalMethod.getParameterCount() &&
                                beanMethod.getParameterTypes()[0].equals(originalMethod.getParameterTypes()[0]))
                            methodMap.put(annotation.order(), beanMethod);
                    }
                }
            }
            handlerContainerMap.put(originalClass, new HandlerContainer(k, bean, new TreeMap<>(methodMap)));
        }

        return new HandlerManagerImpl(handlerContainerMap, executorService);
    }

    private class HandlerContainer {
        String handlerBeanName;
        Class handlerClass;
        Object handlerBean;
        Map<Integer, Method> methodMap = new LinkedHashMap<>();

        public HandlerContainer(String handlerBeanName, Object handlerBean, Map<Integer, Method> methodMap) {
            this.handlerBeanName = handlerBeanName;
            this.handlerClass = handlerBean.getClass();
            this.handlerBean = handlerBean;
            this.methodMap = methodMap;
        }
    }

    private static class HandlerManagerImpl implements HandlerManager {
        private final ExecutorService executorService;

        private final Map<Class<?>, HandlerContainer> handlerContainerMap;

        private HandlerManagerImpl(Map<Class<?>, HandlerContainer> handlerContainerMap, ExecutorService executorService) {
            this.handlerContainerMap = handlerContainerMap;
            this.executorService = executorService;
        }

        private final SelfExpiringMap<String, HandlerContext<?>> existingHandlerContextMap = new SelfExpiringHashMap<>();

        public DeferredResult<HandlerContext<?>> execute(
                Class<?> beanClass, String callerUniqueId, RequestDto<?> requestDto) {
            DeferredResult deferredResult = new DeferredResult();

            FinalContainer<CompletableFuture> futures = new FinalContainer<>();
            HandlerContainer handlerContainer = handlerContainerMap.get(beanClass);

            futures.set(CompletableFuture.supplyAsync(() -> {
                if (handlerContainerMap.containsKey(callerUniqueId))
                    throw new RuntimeException("duplicate id!");
                HandlerContext<?> handlerContext = BaseHandlerContext.create(
                        handlerContainer.handlerBeanName, callerUniqueId, requestDto);
                existingHandlerContextMap.put(callerUniqueId, handlerContext, 30_000l);
                return handlerContext;
            }));

            if (handlerContainer != null) {
                handlerContainer.methodMap.entrySet().forEach(entry -> {
                    if (entry.getValue().getReturnType().isAssignableFrom(CompletableFuture.class)) {
                        futures.set(futures.get().thenComposeAsync(ctx -> {
                                    try {
                                        return entry.getValue().invoke(handlerContainer.handlerBean, ctx);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        throw new RuntimeException("invoke http call exception", e);
                                    }
                                }, executorService)
                                .thenApplyAsync(
                                        httpResponse -> {
                                            HandlerContext context = existingHandlerContextMap.get(callerUniqueId);
                                            context.put(entry.getValue().getName(), httpResponse);
                                            return context;
                                        }, executorService));
                    } else {
                        futures.set(futures.get().thenApplyAsync(ctx -> {
                            try {
                                entry.getValue().invoke(handlerContainer.handlerBean, ctx);
                                return ctx;
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException("invoke method call exception", e);
                            }
                        }, executorService));
                    }
                });
            }

            futures.get().thenAcceptAsync(ctx -> {
                /*existingHandlerContextMap.remove(callerUniqueId);*/
                deferredResult.setResult(ctx);
            }).exceptionallyAsync(ex -> {
                HandlerContext handlerContext = existingHandlerContextMap.remove(callerUniqueId);
                if (ex != null && ex instanceof Throwable) {
                    if (ex instanceof CompletionException) {
                        Throwable cause = ((CompletionException) ex).getCause();
                        if(cause instanceof HandlerRuntimeException)
                            deferredResult.setErrorResult(cause);
                        else
                            deferredResult.setErrorResult(new HandlerRuntimeException(
                                    handlerContext,
                                    HandlerErrorTypes.UNKNOWN.unknownError("unknown error ocurred!"),
                                    (Throwable) ex));
                    } else {
                        deferredResult.setErrorResult(new HandlerRuntimeException(
                                handlerContext,
                                HandlerErrorTypes.UNKNOWN.unknownError("unknown error ocurred!"),
                                (Throwable) ex));
                    }
                } else {
                    deferredResult.setErrorResult(new HandlerRuntimeException(
                            handlerContext,
                            HandlerErrorTypes.UNKNOWN.unknownError("unknown error ocurred!"),
                            (Throwable) ex));
                }
                return null;
            });

            return deferredResult;
        }
    }

    @Setter
    @Component
    @ConfigurationProperties(prefix = "ir.piana.dev.common.reactive-core")
    static class ReactiveCore {
        private int threadPoolSize;
    }
}
