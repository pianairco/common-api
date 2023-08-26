package ir.piana.dev.common.handler;

public enum HandlerErrorTypes {
    CANCELLED(1),
    UNKNOWN(2),
    INVALID_ARGUMENT(3),
    DEADLINE_EXCEEDED(4),
    NOT_FOUND(5),
    ALREADY_EXISTS(6),
    PERMISSION_DENIED(7),
    RESOURCE_EXHAUSTED(8),
    FAILED_PRECONDITION(9),
    ABORTED(10),
    OUT_OF_RANGE(11),
    UNIMPLEMENTED(12),
    INTERNAL(13),
    UNAVAILABLE(14),
    DATA_LOSS(15),
    UNAUTHENTICATED(16);

    private final int code;

    HandlerErrorTypes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static HandlerErrorTypes byCode(int code) {
        for (HandlerErrorTypes errorType : HandlerErrorTypes.values()) {
            if (errorType.code == code)
                return errorType;
        }
        return HandlerErrorTypes.UNKNOWN;
    }

    public DetailedError generateError(String message, DetailedError.ErrorTypes errorType) {
        return new DetailedError(this.code, message, errorType);
    }

    public DetailedError internalError(String message) {
        return new DetailedError(this.code, message, DetailedError.ErrorTypes.INTERNAL);
    }

    public DetailedError externalServerCallError(String message) {
        return new DetailedError(this.code, message, DetailedError.ErrorTypes.EXTERNAL_SERVER_CALL);
    }

    public DetailedError databaseError(String message) {
        return new DetailedError(this.code, message, DetailedError.ErrorTypes.DATABASE);
    }

    public DetailedError badRequestError(String message) {
        return new DetailedError(this.code, message, DetailedError.ErrorTypes.BAD_REQUEST);
    }

    public DetailedError businessError(String message) {
        return new DetailedError(this.code, message, DetailedError.ErrorTypes.BUSINESS);
    }

    public DetailedError unknownError(String message) {
        return new DetailedError(this.code, message, DetailedError.ErrorTypes.UNKNOWN);
    }
}
