package ir.piana.dev.common.handler;

public class HandlerRuntimeException extends RuntimeException {
    private boolean responded;
    private HandlerDetailedError detailedError;
    private HandlerContext context;

    HandlerRuntimeException(
            HandlerContext context,
            HandlerDetailedError detailedError,
            Throwable throwable) {
        super(detailedError.getErrorMessageKey(), throwable);
        this.responded = context.responded();
        this.detailedError = detailedError;
        this.context = context;
    }

    public boolean isResponded() {
        return responded;
    }

    public HandlerDetailedError getDetailedError() {
        return detailedError;
    }

    HandlerContext getContext() {
        return context;
    }
}
