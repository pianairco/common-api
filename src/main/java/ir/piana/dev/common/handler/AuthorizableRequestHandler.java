package ir.piana.dev.common.handler;

import ir.piana.dev.common.auth.RequiredRoles;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AuthorizableRequestHandler<Req, Res> implements RequestHandler<Req, Res> {
    protected final ContextLogger contextLogger;
    protected final ContextLoggerProvider contextLoggerProvider;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    protected HandlerResponseBuilder responseBuilder;
    @Autowired
    protected HandlerRuntimeExceptionThrower thrower;

    private RequiredRoles requiredRoles = new RequiredRoles();

    protected AuthorizableRequestHandler(
            ContextLoggerProvider contextLoggerProvider) {
        this.contextLoggerProvider = contextLoggerProvider;
        this.contextLogger = contextLoggerProvider.registerLogger(this.getClass());
//        this.requiredRoles = requiredRoles();
    }

    final void setRequiredRoles(RequiredRoles requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    final void authenticate(
            HandlerRequest<Req> handlerRequest, HandlerInterStateTransporter transporter) {
        transporter.setUserAuthentication(authenticationManager.get(transporter.getSessionId()));
    }

    final void authorize(
            HandlerRequest<Req> handlerRequest, HandlerInterStateTransporter transporter) {
        if (requiredRoles.isAllRequired()) {
            if (!transporter.getUserAuthentication().getUserAuthorization().hasAllRole(requiredRoles.getRoles()))
                thrower.proceed(HandlerErrorType.PERMISSION_DENIED.generateDetailedError("permission.denied"));
        } else {
            if (!transporter.getUserAuthentication().getUserAuthorization().hasAnyRole(requiredRoles.getRoles()))
                thrower.proceed(HandlerErrorType.PERMISSION_DENIED.generateDetailedError("permission.denied"));
        }
    }
}
