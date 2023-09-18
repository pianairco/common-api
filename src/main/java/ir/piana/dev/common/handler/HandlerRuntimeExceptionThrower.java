package ir.piana.dev.common.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandlerRuntimeExceptionThrower {
    @Autowired
    private HandlerContextThreadLocalProvider handlerContextThreadLocalProvider;

    public void proceed(HandlerDetailedError detailedError) {
        proceed(detailedError, null);
    }

    public void proceed(HandlerDetailedError detailedError, Throwable throwable) throws HandlerRuntimeException {
        HandlerContext handlerContext = handlerContextThreadLocalProvider.get();
        throw new HandlerRuntimeException(handlerContext, detailedError, throwable);
    }

    public HandlerRuntimeException generate(HandlerDetailedError detailedError) {
        return generate(detailedError, null);
    }

    public HandlerRuntimeException generate(HandlerDetailedError detailedError, Throwable throwable) {
        HandlerContext handlerContext = handlerContextThreadLocalProvider.get();
        return new HandlerRuntimeException(handlerContext, detailedError, throwable);
    }
}
