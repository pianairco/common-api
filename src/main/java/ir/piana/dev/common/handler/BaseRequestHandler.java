package ir.piana.dev.common.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class BaseRequestHandler<Req, Res> implements RequestHandler<Req, Res> {
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
