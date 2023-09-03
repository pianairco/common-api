package ir.piana.dev.common.handler;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class HandlerDetailedError {
    private HandlerErrorType errorType;
    private String errorMessageKey;
    private Object[] params;

    public HandlerDetailedError(HandlerErrorType errorType, String errorMessageKey, Object... params) {
        this.errorType = errorType;
        this.errorMessageKey = errorMessageKey;
        this.params = params;
    }

    public HandlerErrorType getErrorType() {
        return errorType;
    }

    public String getErrorMessageKey() {
        return errorMessageKey;
    }

    public Object[] getParams() {
        return params;
    }

    public ThrowableError toThrowableError(MessageSource messageSource) {
        return new ThrowableError(errorType,
                messageSource.getMessage(errorMessageKey, params, errorMessageKey, Locale.getDefault()));
    }

    public static class ThrowableError {
        private HandlerErrorType type;
        private String message;

        public ThrowableError(HandlerErrorType type, String message) {
            this.type = type;
            this.message = message;
        }

        public HandlerErrorType getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }
    }
}
