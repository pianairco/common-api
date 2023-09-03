package ir.piana.dev.common.handler;

public interface HandlerManager {
    <Res> DeferredResult<HandlerResponse<Res>> execute(
            Class<?> beanClass, HandlerRequest<?> handlerRequest);

    /*DeferredResult<HandlerContext<?>> execute(
            Class<?> beanClass, String callerUniqueId, HandlerRequest<?> handlerRequest);*/
}
