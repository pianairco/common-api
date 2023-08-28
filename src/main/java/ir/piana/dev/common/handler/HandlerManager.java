package ir.piana.dev.common.handler;

public interface HandlerManager {
    DeferredResult<HandlerContext<?>> execute(
            Class<?> beanClass, String callerUniqueId, HandlerRequest<?> handlerRequest);
}
