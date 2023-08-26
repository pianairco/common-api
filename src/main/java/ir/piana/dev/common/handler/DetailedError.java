package ir.piana.dev.common.handler;

public class DetailedError {
    private int errorCode;
    private String errorMessage;
    private ErrorTypes errorTypes;

    public DetailedError(int errorCode, String errorMessage, ErrorTypes errorTypes) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorTypes = errorTypes;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorTypes getErrorTypes() {
        return errorTypes;
    }

    public static enum ErrorTypes {
        INTERNAL("Internal"),
        DATABASE("Database"),
        BUSINESS("Business"),
        EXTERNAL_SERVER_CALL("ExternalServerCall"),
        BAD_REQUEST("BadRequest"),
        UNKNOWN("Unknown");
        private String identifier;

        ErrorTypes(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }
    }
}
