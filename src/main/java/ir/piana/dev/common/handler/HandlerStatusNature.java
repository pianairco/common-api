package ir.piana.dev.common.handler;

public enum HandlerStatusNature {
    UNKNOWN(0, "unknown"),
    INFORMATION(1, "information"),
    SUCCESS(2, "success"),
    REDIRECTION(3, "redirection"),
    CLIENT_ERROR(4, "clientError"),
    SERVER_ERROR(5, "serverError");

    private int code;
    private String name;

    HandlerStatusNature(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
