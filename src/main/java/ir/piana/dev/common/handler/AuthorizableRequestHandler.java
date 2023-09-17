package ir.piana.dev.common.handler;

import ir.piana.dev.common.auth.RequiredRoles;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AuthorizableRequestHandler<Req, Res> extends AuthenticableRequestHandler<Req, Res> {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    protected HandlerResponseBuilder responseBuilder;
    @Autowired
    protected HandlerRuntimeExceptionThrower thrower;

    private RequiredRoles requiredRoles = new RequiredRoles();

    protected AuthorizableRequestHandler(
            ContextLoggerProvider contextLoggerProvider) {
        super(contextLoggerProvider);
//        this.requiredRoles = requiredRoles();
    }

    final void setRequiredRoles(RequiredRoles requiredRoles) {
        this.requiredRoles = requiredRoles;
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
