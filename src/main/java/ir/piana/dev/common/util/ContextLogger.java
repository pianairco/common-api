package ir.piana.dev.common.util;

import ir.piana.dev.common.handler.HandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContextLogger {
    private Logger logger;

    private ContextLogger(Logger logger) {
        this.logger = logger;
    }

    public static ContextLogger getLogger(Class theClass) {
        return new ContextLogger(LoggerFactory.getLogger(theClass));
    }
    public void error(HandlerContext<?> context, Throwable throwable) {
        logger.error("{}({}): {}", context.handlerName(), context.uniqueId(), throwable.getMessage());
    }

    public void error(HandlerContext<?> context, String message, Object... params) {
        logger.error("{}({}): " + message, mixParams(params, context.handlerName(), context.uniqueId()));
    }

    public void debug(HandlerContext<?> context, Throwable throwable) {
        logger.debug("{}({}): {}", context.handlerName(), context.uniqueId(), throwable.getMessage());
    }

    private Object[] mixParams(Object[] externalObjs, Object... internalObjs) {
        List<Object> objects = new ArrayList<>();
        objects.addAll(Arrays.stream(internalObjs).toList());
        objects.addAll(Arrays.stream(externalObjs).toList());
        return objects.toArray();
    }

    public void debug(HandlerContext<?> context, String message, Object... params) {
        /*List<Object> objects = new ArrayList<>();
        objects.addAll(Arrays.asList(context.handlerName(), context.uniqueId()));
        objects.addAll(Arrays.stream(params).toList());*/

        logger.debug("{}({}): " + message, mixParams(params, context.handlerName(), context.uniqueId()));
    }

    public void info(HandlerContext<?> context, Throwable throwable) {
        logger.info("{}({}): {}", context.handlerName(), context.uniqueId(), throwable.getMessage());
    }

    public void info(HandlerContext<?> context, String message, Object... params) {
        logger.info("{}({}): " + message, mixParams(params, context.handlerName(), context.uniqueId()));
    }

    public void trace(HandlerContext<?> context, Throwable throwable) {
        logger.trace("{}({}): {}", context.handlerName(), context.uniqueId(), throwable.getMessage());
    }

    public void trace(HandlerContext<?> context, String message, Object... params) {
        logger.trace("{}({}): " + message, mixParams(params, context.handlerName(), context.uniqueId()));
    }

    public void warn(HandlerContext<?> context, Throwable throwable) {
        logger.warn("{}({}): {}", context.handlerName(), context.uniqueId(), throwable.getMessage());
    }

    public void warn(HandlerContext<?> context, String message, Object... params) {
        logger.warn("{}({}): " + message, mixParams(params, context.handlerName(), context.uniqueId()));
    }
}
