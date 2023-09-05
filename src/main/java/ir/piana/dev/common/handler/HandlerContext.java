package ir.piana.dev.common.handler;

public interface HandlerContext<Req> {
    String handlerName();
    boolean responded();
    long uniqueId();
    HandlerRequest<Req> request();
//    <Res> HandlerContext<Req> addHandlerResponse(HandlerResponse<Res> resultDto);
//    <Res> HandlerResponse<Res> handlerResponse();
    <T> HandlerContext<Req> put(String key, T val);
    <T> T get(String key);
    HandlerInterStateTransporter getInterstateTransporter();
}
