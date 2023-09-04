package ir.piana.dev.common.handler;

public interface HandlerManager {
    DeferredResult<HandlerResponse> execute(
            Class<?> beanClass, HandlerRequest<?> handlerRequest);

    /*DeferredResult<HandlerContext<?>> execute(
            Class<?> beanClass, String callerUniqueId, HandlerRequest<?> handlerRequest);*/
}
