package ir.piana.dev.common.handler;

import ir.piana.dev.common.service.UniqueIdService;
import ir.piana.dev.common.util.*;
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

//    private final ContextLogger logger = ContextLogger.getLogger(this.getClass());

    @Bean("reactiveCommonThreadPool")
    public ExecutorService reactiveCommonThreadPool(@Autowired ReactiveCore reactiveCore) {
        return Executors.newFixedThreadPool(reactiveCore.threadPoolSize);
    }

    @Bean
    Method provideResponseMethod() {
        Method provideResponseMethod;
        try {
            return RequestHandler.class.getDeclaredMethod("provideResponse", HandlerRequest.class, HandlerInterStateTransporter.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    ThreadLocal threadLocal;

    @Bean
    HandlerManager getHandlerManager(
            ApplicationContext applicationContext,
            HandlerRuntimeExceptionThrower handlerRuntimeExceptionThrower,
            Method provideResponseMethod,
            @Qualifier("reactiveCommonThreadPool") ExecutorService executorService,
            HandlerContextThreadLocalProvider handlerContextThreadLocalProvider,
            ContextLoggerProvider contextLoggerProvider)
            throws ClassNotFoundException {
        ContextLogger logger = contextLoggerProvider.registerLogger(HandlerManagerAutoConfiguration.class);
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Handler.class);

        Map<Class<?>, HandlerContainer> handlerContainerMap = new LinkedHashMap<>();
        final List<Map.Entry> classNotFounds = new ArrayList<>();
        final Map<String, Class> classFounds = new LinkedHashMap<>();

        beansWithAnnotation.entrySet().stream().filter(entry -> {
            if (!(entry.getValue() instanceof RequestHandler)) {
                logger.error(null, "handler {} must be implement RequestHandler", entry.getKey());
                return false;
            }
            try {
                if (entry.getValue().getClass().getName().contains("$$"))
                    classFounds.put(entry.getKey(),
                            Class.forName(entry.getValue().getClass().getGenericSuperclass().getTypeName()));
                else
                    classFounds.put(entry.getKey(),
                            Class.forName(entry.getValue().getClass().getTypeName()));
                return true;
            } catch (ClassNotFoundException e) {
                classNotFounds.add(entry);
            }
            return false;
        }).forEach(entry -> {
            Map<Integer, Method> chainStepMethodMap = new LinkedHashMap<>();
            Map<Integer, Method> rollbackMethodMap = new LinkedHashMap<>();
            Class originalClass = classFounds.get(entry.getKey());

            Method[] originalDeclaredMethods = originalClass.getDeclaredMethods();
            Method[] beanDeclaredMethods = originalClass.getDeclaredMethods();
            for (Method originalMethod : originalDeclaredMethods) {
                if (originalMethod.equals(provideResponseMethod))
                    continue;
                ChainStep chainStep = originalMethod.getAnnotation(ChainStep.class);
                AssignedRollback assignedRollback = originalMethod.getAnnotation(AssignedRollback.class);
                if (chainStep != null && originalMethod.getParameterCount() == 2 &&
                        originalMethod.getParameterTypes()[0].isAssignableFrom(HandlerRequest.class) &&
                        originalMethod.getParameterTypes()[1].isAssignableFrom(HandlerInterStateTransporter.class) &&
                        (originalMethod.getReturnType().isAssignableFrom(void.class) ||
                                originalMethod.getReturnType().isAssignableFrom(HandlerResponse.class) ||
                                originalMethod.getReturnType().isAssignableFrom(CompletableFuture.class))) {
                    if (chainStepMethodMap.containsKey(chainStep.order()))
                        throw new RuntimeException("ChainStep by same order : " + chainStep.order() + " on " + entry.getKey());
                    for (Method beanMethod : beanDeclaredMethods) {
                        if (beanMethod.getName().equals(originalMethod.getName()) &&
                                beanMethod.getReturnType().equals(originalMethod.getReturnType()) &&
                                beanMethod.getParameterCount() == originalMethod.getParameterCount() &&
                                beanMethod.getParameterTypes()[0].equals(originalMethod.getParameterTypes()[0]))
                            chainStepMethodMap.put(chainStep.order(), beanMethod);
                    }
                } else if (assignedRollback != null && originalMethod.getParameterCount() == 2 &&
                        originalMethod.getParameterTypes()[0].isAssignableFrom(HandlerRequest.class) &&
                        originalMethod.getParameterTypes()[1].isAssignableFrom(HandlerInterStateTransporter.class) &&
                        (originalMethod.getReturnType().isAssignableFrom(void.class))) {
                    if (rollbackMethodMap.containsKey(assignedRollback.matchedOrder()))
                        throw new RuntimeException("AssignedRollback by same order : " + assignedRollback.matchedOrder() + " on " + entry.getKey());
                    for (Method beanMethod : beanDeclaredMethods) {
                        if (beanMethod.getName().equals(originalMethod.getName()) &&
                                beanMethod.getReturnType().equals(originalMethod.getReturnType()) &&
                                beanMethod.getParameterCount() == originalMethod.getParameterCount() &&
                                beanMethod.getParameterTypes()[0].equals(originalMethod.getParameterTypes()[0]))
                            rollbackMethodMap.put(assignedRollback.matchedOrder(), beanMethod);
                    }
                }
            }
            handlerContainerMap.put(originalClass, new HandlerContainer(
                    entry.getKey(),
                    entry.getValue(),
                    new TreeMap<>(chainStepMethodMap),
                    new TreeMap<>(rollbackMethodMap)));
        });

        return new HandlerManagerImpl(handlerContainerMap, executorService,
                handlerRuntimeExceptionThrower,
                handlerContextThreadLocalProvider,
                contextLoggerProvider,
                contextLoggerProvider.registerLogger(HandlerManager.class));
    }

    private class HandlerContainer {
        String handlerBeanName;
        Class handlerClass;
        Object handlerBean;
        Map<Integer, Method> chainStepMethodMap = new LinkedHashMap<>();
        Map<Integer, Method> rollbackMethodMap = new LinkedHashMap<>();

        public HandlerContainer(
                String handlerBeanName, Object handlerBean,
                Map<Integer, Method> chainStepMethodMap,
                Map<Integer, Method> rollbackMethodMap) {
            this.handlerBeanName = handlerBeanName;
            this.handlerClass = handlerBean.getClass();
            this.handlerBean = handlerBean;
            this.chainStepMethodMap = chainStepMethodMap;
            this.rollbackMethodMap = rollbackMethodMap;
        }
    }

    private static class HandlerManagerImpl implements HandlerManager {
        private ContextLoggerProvider contextLoggerProvider;
        private HandlerContextThreadLocalProvider handlerContextThreadLocalProvider;
        private HandlerRuntimeExceptionThrower handlerRuntimeExceptionThrower;
        private final ContextLogger logger;;
        private final ExecutorService executorService;

        private final Map<Class<?>, HandlerContainer> handlerContainerMap;

        private HandlerManagerImpl(Map<Class<?>, HandlerContainer> handlerContainerMap,
                                   ExecutorService executorService,
                                   HandlerRuntimeExceptionThrower handlerRuntimeExceptionThrower,
                                   HandlerContextThreadLocalProvider handlerContextThreadLocalProvider,
                                   ContextLoggerProvider contextLoggerProvider,
                                   ContextLogger logger) {
            this.handlerContainerMap = handlerContainerMap;
            this.executorService = executorService;
            this.handlerRuntimeExceptionThrower = handlerRuntimeExceptionThrower;
            this.handlerContextThreadLocalProvider = handlerContextThreadLocalProvider;
            this.contextLoggerProvider = contextLoggerProvider;
            this.logger = logger;
            rollbackExecutors = Executors.newFixedThreadPool(10);
        }

        private final SelfExpiringMap<Long, HandlerContext<?>> existingHandlerContextMap = new SelfExpiringHashMap<>();

        ExecutorService rollbackExecutors;

        @Autowired
        private UniqueIdService uniqueIdService;

        @Autowired
        private Method provideResponseMethod;

        @Override
        public <Res> DeferredResult<HandlerResponse<Res>> execute(
                Class<?> beanClass, HandlerRequest<?> handlerRequest) {
            DeferredResult deferredResult = new DeferredResult();
            final FinalContainer<CompletableFuture> futures = new FinalContainer<>();
            final HandlerContainer handlerContainer = handlerContainerMap.get(beanClass);
            final FinalContainer<Long> uniqueIdContainer = new FinalContainer<>();
            final FinalContainer<Boolean> responseGenerated = new FinalContainer<>(false);

            futures.set(CompletableFuture.supplyAsync(() -> {
                uniqueIdContainer.set(uniqueIdService.getId());
                if (handlerContainerMap.containsKey(uniqueIdContainer.get()))
                    throw new RuntimeException("duplicate id!");
                HandlerContext<?> handlerContext = BaseHandlerContext.create(
                        handlerContainer.handlerBeanName, uniqueIdContainer.get(), handlerRequest);
                existingHandlerContextMap.put(handlerContext.uniqueId(), handlerContext, 30_000l);
                return handlerContext;
            }));

            final List<Method> rollbackMethods = new ArrayList<>();

            if (handlerContainer != null) {
                handlerContainer.chainStepMethodMap.entrySet().forEach(entry -> {
                    if (entry.getValue().getReturnType().isAssignableFrom(CompletableFuture.class)) {
                        futures.set(futures.get().thenComposeAsync(ctx -> {
                                    try {
                                        handlerContextThreadLocalProvider.set((HandlerContext) ctx);
                                        return entry.getValue().invoke(handlerContainer.handlerBean,
                                                ((HandlerContext) ctx).request(),
                                                ((HandlerContext) ctx).getInterstateTransporter());
                                    } catch (IllegalAccessException e) {
                                        logger.error(e);
                                        throw new HandlerRuntimeException(
                                                (HandlerContext<?>) ctx,
                                                HandlerErrorType.INTERNAL.generateDetailedError(
                                                        "invoke method call exception"), e);
                                    } catch (InvocationTargetException e) {
                                        if (e.getTargetException() instanceof HandlerRuntimeException)
                                            throw (HandlerRuntimeException) e.getTargetException();
                                        throw new RuntimeException(e.getTargetException());
//                                handlerExceptionThrower.proceed(HandlerErrorType.INTERNAL.generateDetailedError("error.unknown"));
                                    } finally {
                                        handlerContextThreadLocalProvider.remove();
                                    }
                                }, executorService)
                                .thenApplyAsync(
                                        httpResponse -> {
                                            HandlerContext context = existingHandlerContextMap.get(uniqueIdContainer.get());
                                            context.put(entry.getValue().getName(), httpResponse);
                                            return context;
                                        }, executorService));
                    } else {
                        futures.set(futures.get().thenApplyAsync(ctx -> {
                            try {
                                handlerContextThreadLocalProvider.set((HandlerContext) ctx);
                                Object returnValue = entry.getValue().invoke(handlerContainer.handlerBean,
                                        ((HandlerContext) ctx).request(),
                                        ((HandlerContext) ctx).getInterstateTransporter());
                                /*if (returnValue instanceof HandlerResponse<?>) {
                                    ((BaseHandlerContext<Object>)existingHandlerContextMap.get(uniqueIdContainer.get()))
                                            .addHandlerResponse((HandlerResponse) returnValue);
                                    responseGenerated.set(true);
                                }*/
                                return existingHandlerContextMap.get(uniqueIdContainer.get());
                            } catch (IllegalAccessException e) {
                                throw new HandlerRuntimeException(
                                        (HandlerContext<?>) ctx,
                                        HandlerErrorType.INTERNAL.generateDetailedError("invoke method call exception"), e);
                            } catch (InvocationTargetException e) {
                                if (e.getTargetException() instanceof HandlerRuntimeException)
                                    throw (HandlerRuntimeException) e.getTargetException();
                                throw new RuntimeException(e.getTargetException());
//                                handlerExceptionThrower.proceed(HandlerErrorType.INTERNAL.generateDetailedError("error.unknown"));
                            } finally {
                                handlerContextThreadLocalProvider.remove();
                            }
                        }, executorService));
                    }
                    if (handlerContainer.rollbackMethodMap.containsKey(entry.getKey()))
                        rollbackMethods.add(handlerContainer.rollbackMethodMap.get(entry.getKey()));
                });
            }

            futures.get().thenAcceptAsync(ctx -> {
                try {
                    handlerContextThreadLocalProvider.set((HandlerContext) ctx);
                    HandlerResponse handlerResponse = (HandlerResponse) provideResponseMethod.invoke(
                            handlerContainer.handlerBean,
                            ((HandlerContext) ctx).request(),
                            ((HandlerContext) ctx).getInterstateTransporter());
                    if (((HandlerContext) ctx).responded()) {
                        logger.error("Already sent response!");
                    } else {
                        deferredResult.setResult(handlerResponse);
                    }
                } catch (IllegalAccessException e) {
                    throw new HandlerRuntimeException(
                            (HandlerContext<?>) ctx,
                            HandlerErrorType.INTERNAL.generateDetailedError("invoke method call exception"), e);
                } catch (InvocationTargetException e) {
                    if (e.getTargetException() instanceof HandlerRuntimeException)
                        throw (HandlerRuntimeException) e.getTargetException();
                    throw new RuntimeException(e.getTargetException());
//                                handlerExceptionThrower.proceed(HandlerErrorType.INTERNAL.generateDetailedError("error.unknown"));
                } finally {
                    handlerContextThreadLocalProvider.remove();
                }
                /*existingHandlerContextMap.remove(callerUniqueId);*/
            }).exceptionallyAsync(ex -> {
                HandlerContext handlerContext = existingHandlerContextMap.remove(uniqueIdContainer.get());
                try {
                    handlerContextThreadLocalProvider.set(handlerContext);
                    /*if (ex != null) {*/
                    Throwable cause = (Throwable) ex;
                    if (ex instanceof CompletionException) {
                        cause = ((CompletionException) ex).getCause();
                    }
                    if (cause instanceof HandlerRuntimeException) {
                        logger.error(
                                cause.getMessage(),
                                ((HandlerRuntimeException) cause).getDetailedError().getParams());
                        deferredResult.setErrorResult(cause);
                    } else {
                        HandlerRuntimeException handlerException = new HandlerRuntimeException(
                                handlerContext,
                                HandlerErrorType.UNKNOWN.generateDetailedError("unknown error occurred!"),
                                cause);
                        deferredResult.setErrorResult(handlerException);
                    }

                    /**
                     * ToDo: rollback strategy should be stronger than this
                     */
                    rollbackExecutors.execute(() -> doRollback(
                            handlerContainer.handlerBean, rollbackMethods, handlerContext));

                    return null;
                } finally {
                    handlerContextThreadLocalProvider.remove();
                }
            }, executorService);

            return deferredResult;
        }

        private void doRollback(
                Object handlerBean,
                List<Method> rollbackMethods,
                HandlerContext handlerContext) {
            for (Method rollbackMethod : rollbackMethods) {
                try {
                    handlerContextThreadLocalProvider.set(handlerContext);
                    rollbackMethod.invoke(handlerBean,
                            handlerContext.request(),
                            handlerContext.getInterstateTransporter());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } finally {
                    handlerContextThreadLocalProvider.remove();
                }
            }
        }
    }

    @Setter
    @Component
    @ConfigurationProperties(prefix = "ir.piana.dev.common.reactive-core")
    static class ReactiveCore {
        private int threadPoolSize;
    }
}
