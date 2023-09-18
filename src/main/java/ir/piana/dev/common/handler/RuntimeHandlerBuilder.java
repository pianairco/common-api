package ir.piana.dev.common.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RuntimeHandlerBuilder {
    @Autowired
    private ContextLoggerProvider contextLoggerProvider;

    @Autowired
    private AnnotationConfigApplicationContext applicationContext;

    public RequestHandler baseRequestHandler (
            RequestHandler requestHandler) {
        final RequestHandler target = new BaseRequestHandler(contextLoggerProvider) {
            @Override
            public CommonResponse provideResponse(
                    HandlerRequest handlerRequest, HandlerInterStateTransporter transporter) {
                return requestHandler.provideResponse(handlerRequest, transporter);
            }
        };
        applicationContext.registerBean(
                (Class<? super RequestHandler>) target.getClass(), () -> target);

        return (RequestHandler) applicationContext.getBean(
                (Class<? super RequestHandler>) target.getClass());
    }
}
