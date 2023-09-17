package ir.piana.dev.common.handler;

import ir.piana.dev.common.auth.RequiredRoles;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AuthenticableRequestHandler<Req, Res> extends BaseRequestHandler<Req, Res> {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    protected HandlerResponseBuilder responseBuilder;
    @Autowired
    protected HandlerRuntimeExceptionThrower thrower;

    private RequiredRoles requiredRoles = new RequiredRoles();

    protected AuthenticableRequestHandler(
            ContextLoggerProvider contextLoggerProvider) {
    super(contextLoggerProvider);
    }

    final void authenticate(
            HandlerRequest<Req> handlerRequest, HandlerInterStateTransporter transporter) {
        transporter.setUserAuthentication(authenticationManager.get(transporter.getSessionId()));
    }
}
