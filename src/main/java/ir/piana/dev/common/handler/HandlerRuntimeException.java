package ir.piana.dev.common.handler;

public class HandlerRuntimeException extends RuntimeException {
    private HandlerContext context;

    public HandlerRuntimeException(
            HandlerContext context,
            DetailedError detailedError,
            Throwable throwable) {
        super(detailedError.getErrorMessage(), throwable);
        this.context = context;
    }

    public HandlerContext getContext() {
        return context;
    }
}
