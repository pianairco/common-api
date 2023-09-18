package ir.piana.dev.common.handler;

public interface RequestHandler<Req, Res> {
    CommonResponse<Res> provideResponse(
            HandlerRequest<Req> handlerRequest, HandlerInterStateTransporter transporter) throws HandlerRuntimeException;
}
