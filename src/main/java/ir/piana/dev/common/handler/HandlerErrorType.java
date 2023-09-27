package ir.piana.dev.common.handler;

public enum HandlerErrorType {
    OK(0, HandlerStatusNature.SUCCESS),
    CANCELLED(1, HandlerStatusNature.CLIENT_ERROR),
    UNKNOWN(2, HandlerStatusNature.SERVER_ERROR),
    INVALID_ARGUMENT(3, HandlerStatusNature.CLIENT_ERROR),
    DEADLINE_EXCEEDED(4, HandlerStatusNature.SERVER_ERROR),
    NOT_FOUND(5, HandlerStatusNature.CLIENT_ERROR),
    ALREADY_EXISTS(6, HandlerStatusNature.CLIENT_ERROR),
    UNAUTHENTICATED(7, HandlerStatusNature.CLIENT_ERROR),
    PERMISSION_DENIED(8, HandlerStatusNature.CLIENT_ERROR),
    RESOURCE_EXHAUSTED(9, HandlerStatusNature.SERVER_ERROR),
    FAILED_PRECONDITION(10, HandlerStatusNature.CLIENT_ERROR),
    ABORTED(11, HandlerStatusNature.CLIENT_ERROR),
    OUT_OF_RANGE(12, HandlerStatusNature.CLIENT_ERROR),
    UNIMPLEMENTED(13, HandlerStatusNature.SERVER_ERROR),
    INTERNAL(14, HandlerStatusNature.SERVER_ERROR),
    UNAVAILABLE(15, HandlerStatusNature.SERVER_ERROR),
    DATA_LOSS(16, HandlerStatusNature.SERVER_ERROR),
    REDIRECT(17, HandlerStatusNature.SUCCESS);

    private final int code;
    private final HandlerStatusNature handlerStatusNature;

    HandlerErrorType(int code, HandlerStatusNature handlerStatusNature) {
        this.code = code;
        this.handlerStatusNature = handlerStatusNature;
    }

    public int getCode() {
        return code;
    }

    public HandlerStatusNature getHandlerStatusNature() {
        return handlerStatusNature;
    }

    public static HandlerErrorType byCode(int code) {
        for (HandlerErrorType errorType : HandlerErrorType.values()) {
            if (errorType.code == code)
                return errorType;
        }
        return HandlerErrorType.UNKNOWN;
    }

    public HandlerDetailedError generateDetailedError(String messageKey, Object... params) {
        return new HandlerDetailedError(this, messageKey, params);
    }
}
