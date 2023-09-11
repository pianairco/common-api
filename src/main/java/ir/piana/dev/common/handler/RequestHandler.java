package ir.piana.dev.common.handler;

interface RequestHandler<Req, Res> {
    CommonResponse<Res> provideResponse(
            HandlerRequest<Req> handlerRequest, HandlerInterStateTransporter transporter);
}
