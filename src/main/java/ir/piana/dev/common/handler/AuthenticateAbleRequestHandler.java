package ir.piana.dev.common.handler;

import ir.piana.dev.common.auth.AuthenticateAbleResponse;
import ir.piana.dev.common.auth.UserAuthentication;
import ir.piana.dev.jsonparser.json.JsonTarget;

public abstract class AuthenticateAbleRequestHandler<Req> extends AuthorizableRequestHandler<Req, Void> {
    protected AuthenticateAbleRequestHandler(
            ContextLoggerProvider contextLoggerProvider) {
        super(contextLoggerProvider);
    }

    @Override
    public final CommonResponse<Void> provideResponse(
            HandlerRequest<Req> handlerRequest, HandlerInterStateTransporter transporter) {
        AuthenticateAbleResponse response = doAuthenticate(handlerRequest, transporter);
        if (response != null) {
            if (response.isAuthenticated()) {
                authenticationManager.reassign(
                        transporter.getSessionId(),
                        response.getPrincipal(),
                        response.getUserAuthorization());
            }
            if (response.getView() != null) {
                return HandlerModelAndViewResponse.builder()
                        .view(response.getView())
                        .model(response.getModel())
                        .authPhrase(transporter.getSessionId())
                        .build();
            } else {
                return responseBuilder.withoutBody()
                        .setAuthPhrase(transporter.getSessionId())
                        .build();
            }
        }
        throw thrower.generate(HandlerErrorType.INTERNAL.generateDetailedError("authenticated.error"));
    }

    public abstract AuthenticateAbleResponse doAuthenticate(
            HandlerRequest<Req> handlerRequest, HandlerInterStateTransporter transporter);
}
