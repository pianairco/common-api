package ir.piana.dev.common.handler;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRequestHandler<Req> implements RequestHandler<Req> {
    protected final ContextLogger contextLogger;
    protected final ContextLoggerProvider contextLoggerProvider;

    @Autowired
    protected HandlerResponseBuilder responseBuilder;
    @Autowired
    protected HandlerRuntimeExceptionThrower thrower;

    protected BaseRequestHandler(
            ContextLoggerProvider contextLoggerProvider) {
        this.contextLoggerProvider = contextLoggerProvider;
        this.contextLogger = contextLoggerProvider.registerLogger(this.getClass());
    }
}
