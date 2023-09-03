package ir.piana.dev.common.handler;

public interface ContextLogger {


    void error(Throwable throwable);

    void error(HandlerRuntimeException HandlerRuntimeException);

    void error(HandlerDetailedError handlerDetailedError);

    void error(String message, Object... params);

    void debug(Throwable throwable);

    void debug(HandlerRuntimeException HandlerRuntimeException);

    void debug(String message, Object... params);

    void info(Throwable throwable);

    void info(HandlerRuntimeException HandlerRuntimeException);

    void info(String message, Object... params);

    void trace(Throwable throwable);

    void trace(HandlerRuntimeException HandlerRuntimeException);

    void trace(String message, Object... params);

    void warn(Throwable throwable);

    void warn(HandlerRuntimeException HandlerRuntimeException);

    void warn(String message, Object... params);
}
