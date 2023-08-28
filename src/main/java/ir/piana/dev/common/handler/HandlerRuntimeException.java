package ir.piana.dev.common.handler;

public class HandlerRuntimeException extends RuntimeException {
    private HandlerContext context;
    private DetailedError detailedError;

    public HandlerRuntimeException(
            HandlerContext context,
            DetailedError detailedError,
            Throwable throwable) {
        super(detailedError.getErrorMessage(), throwable);
        this.context = context;
        this.detailedError = detailedError;
    }

    public HandlerContext getContext() {
        return context;
    }

    public DetailedError getDetailedError() {
        return detailedError;
    }
}
