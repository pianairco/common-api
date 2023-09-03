package ir.piana.dev.common.handler;

public abstract class BaseRequestHandler<Req> implements RequestHandler<Req> {
    protected ContextLogger contextLogger;
    protected ContextLoggerProvider contextLoggerProvider;

    protected HandlerRuntimeExceptionThrower handlerRuntimeExceptionThrower;

    protected BaseRequestHandler(
            ContextLoggerProvider contextLoggerProvider, HandlerRuntimeExceptionThrower handlerRuntimeExceptionThrower) {
        this.contextLogger = contextLoggerProvider.registerLogger(this.getClass());
        this.contextLoggerProvider = contextLoggerProvider;
        this.handlerRuntimeExceptionThrower = handlerRuntimeExceptionThrower;
    }
}
