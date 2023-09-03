package ir.piana.dev.common.handler;

import org.springframework.stereotype.Component;

@Component
public class HandlerContextThreadLocalProvider {
    private ThreadLocal<HandlerContext> handlerContextThreadLocal = new ThreadLocal<>();

    void set(HandlerContext handlerContext) {
        this.handlerContextThreadLocal.set(handlerContext);
    }

    void remove() {
        this.handlerContextThreadLocal.remove();
    }

    public HandlerContext get() {
        return handlerContextThreadLocal.get();
    }
}
