package ir.piana.dev.common.handler;

interface RequestHandler<Req> {
    HandlerResponse provideResponse(
            HandlerRequest<Req> handlerRequest, HandlerInterStateTransporter transporter);
}
