package ir.piana.dev.common.handler;

import ir.piana.dev.common.util.HandlerInterStateTransporter;

interface RequestHandler<Req> {
    HandlerResponse provideResponse(HandlerRequest<Req> handlerRequest, HandlerInterStateTransporter transporter);
}
