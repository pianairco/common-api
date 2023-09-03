package ir.piana.dev.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ContextLoggerProvider {
    private Map<Class, ContextLogger> loggerMap = new LinkedHashMap<>();

    @Autowired
    private HandlerContextThreadLocalProvider handlerContextThreadLocalProvider;


    @Autowired
    private MessageSource messageSource;

    public ContextLogger registerLogger(Class theClass) {
        ContextLoggerImpl contextLogger = new ContextLoggerImpl(LoggerFactory.getLogger(theClass));
        loggerMap.put(theClass, contextLogger);
        return contextLogger;
    }

    private class ContextLoggerImpl implements ContextLogger {
        private Logger logger;

        private ContextLoggerImpl(Logger logger) {
            this.logger = logger;
        }

        private Object[] mixParams(Object[] externalObjs, Object... internalObjs) {
            List<Object> objects = new ArrayList<>();
            objects.addAll(Arrays.stream(internalObjs).toList());
            objects.addAll(Arrays.stream(externalObjs).toList());
            return objects.toArray();
        }

        @Override
        public void error(Throwable throwable) {
            logger.error("{}({}): {}",
                    handlerContextThreadLocalProvider.get().handlerName(),
                    handlerContextThreadLocalProvider.get().uniqueId(), throwable.getMessage());
        }

        @Override
        public void error(HandlerRuntimeException handlerException) {
            logger.error("{}({}): {}" + handlerException.getDetailedError().getErrorMessageKey(),
                    mixParams(handlerException.getDetailedError().getParams(),
                            handlerException.getContext().handlerName(),
                            handlerException.getContext().uniqueId()));
        }

        @Override
        public void error(HandlerDetailedError handlerDetailedError) {
            logger.error("{}({}): {}" + handlerDetailedError.getErrorMessageKey(),
                    mixParams(handlerDetailedError.getParams(),
                            handlerContextThreadLocalProvider.get().handlerName(),
                            handlerContextThreadLocalProvider.get().uniqueId()));
        }

        @Override
        public void error(String message, Object... params) {
            logger.error("{}({}): " + message, mixParams(params,
                    handlerContextThreadLocalProvider.get().handlerName(),
                    handlerContextThreadLocalProvider.get().uniqueId()));
        }

        @Override
        public void debug(Throwable throwable) {
            logger.debug("{}({}): {}",
                    handlerContextThreadLocalProvider.get().handlerName(),
                    handlerContextThreadLocalProvider.get().uniqueId(), throwable.getMessage());
        }

        @Override
        public void debug(HandlerRuntimeException handlerException) {
            logger.error("{}({}): {}" + handlerException.getDetailedError().getErrorMessageKey(),
                    mixParams(handlerException.getDetailedError().getParams(),
                            handlerException.getContext().handlerName(),
                            handlerException.getContext().uniqueId()));
        }

        @Override
        public void debug(String message, Object... params) {
            logger.debug("{}({}): " + message, mixParams(params,
                    handlerContextThreadLocalProvider.get().handlerName(),
                    handlerContextThreadLocalProvider.get().uniqueId()));
        }

        @Override
        public void info(Throwable throwable) {
            logger.info("{}({}): {}",
                    handlerContextThreadLocalProvider.get().handlerName(),
                    handlerContextThreadLocalProvider.get().uniqueId(), throwable.getMessage());
        }

        @Override
        public void info(HandlerRuntimeException handlerException) {
            logger.error("{}({}): {}" + handlerException.getDetailedError().getErrorMessageKey(),
                    mixParams(handlerException.getDetailedError().getParams(),
                            handlerException.getContext().handlerName(),
                            handlerException.getContext().uniqueId()));
        }

        @Override
        public void info(String message, Object... params) {
            logger.info("{}({}): " + message, mixParams(params,
                    handlerContextThreadLocalProvider.get().handlerName(),
                    handlerContextThreadLocalProvider.get().uniqueId()));
        }

        @Override
        public void trace(Throwable throwable) {
            logger.trace("{}({}): {}",
                    handlerContextThreadLocalProvider.get().handlerName(),
                    handlerContextThreadLocalProvider.get().uniqueId(), throwable.getMessage());
        }

        @Override
        public void trace(HandlerRuntimeException handlerException) {
            logger.error("{}({}): {}" + handlerException.getDetailedError().getErrorMessageKey(),
                    mixParams(handlerException.getDetailedError().getParams(),
                            handlerException.getContext().handlerName(),
                            handlerException.getContext().uniqueId()));
        }

        @Override
        public void trace(String message, Object... params) {
            logger.trace("{}({}): " + message, mixParams(params,
                    handlerContextThreadLocalProvider.get().handlerName(),
                    handlerContextThreadLocalProvider.get().uniqueId()));
        }

        @Override
        public void warn(Throwable throwable) {
            logger.warn("{}({}): {}",
                    handlerContextThreadLocalProvider.get().handlerName(),
                    handlerContextThreadLocalProvider.get().uniqueId(), throwable.getMessage());
        }

        @Override
        public void warn(HandlerRuntimeException handlerException) {
            logger.error("{}({}): {}" + handlerException.getDetailedError().getErrorMessageKey(),
                    mixParams(handlerException.getDetailedError().getParams(),
                            handlerException.getContext().handlerName(),
                            handlerException.getContext().uniqueId()));
        }

        @Override
        public void warn(String message, Object... params) {
            logger.warn("{}({}): " + message, mixParams(params,
                    handlerContextThreadLocalProvider.get().handlerName(),
                    handlerContextThreadLocalProvider.get().uniqueId()));
        }
    }
}
