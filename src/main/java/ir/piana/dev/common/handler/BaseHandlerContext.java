package ir.piana.dev.common.handler;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaseHandlerContext<Req> implements HandlerContext<Req> {
    private final AtomicBoolean responded = new AtomicBoolean(false);
    private final String handlerName;
    private final long uniqueId;
    /*private Map<String, Object> map = new LinkedHashMap<>();*/
//    private HandlerResponse<?> handlerResponse;
    private HandlerInterStateTransporter interStateTransporter;
    private HandlerRequest<Req> handlerRequest;

    public BaseHandlerContext(
            String handlerName,
            long uniqueId,
            HandlerRequest<Req> handlerRequest,
            String sessionId) {
        this.handlerName = handlerName;
        this.uniqueId = uniqueId;
        this.handlerRequest = handlerRequest;
//        this.sessionId = (handlerRequest.getAuthPhrase() == null || ).orElse(UUID.randomUUID().toString());
        this.interStateTransporter = new HandlerInterStateTransporter();
        this.interStateTransporter.setSessionId(sessionId);
    }

    /*public static HandlerContext<?> fromRequest(RequestDto<?> requestDto) {
        return new BaseHandlerContext<>(requestDto);
    }

    public static HandlerContext<?> fromRequest(String handlerBeanName, String uniqueId, RequestDto<?> requestDto) {
        return new BaseHandlerContext<>(requestDto).uniqueId;
    }*/

    public static HandlerContext<?> create(
            String handlerName,
            long uniqueId,
            HandlerRequest<?> handlerRequest,
            String sessionId) {
        return new BaseHandlerContext<>(handlerName, uniqueId, handlerRequest, sessionId);
    }

    @Override
    public String handlerName() {
        return handlerName;
    }

    @Override
    public boolean responded() {
        return responded.getAndSet(true);
    }

    @Override
    public long uniqueId() {
        return uniqueId;
    }

    @Override
    public HandlerRequest<Req> request() {
        return handlerRequest;
    }

    /*public <Res> HandlerContext<Req> addHandlerResponse(HandlerResponse<Res> handlerResponse) {
        this.handlerResponse = handlerResponse;
        return this;
    }*/

    /*@Override
    public <Res> HandlerResponse<Res> handlerResponse() {
        return (HandlerResponse<Res>) handlerResponse;
    }*/

    @Override
    public <T> HandlerContext<Req> put(String key, T val) {
        interStateTransporter.put(key, val);
        return this;
    }

    @Override
    public <T> T get(String key) {
        return (T) interStateTransporter.getValue(key);
    }

    @Override
    public HandlerInterStateTransporter getInterstateTransporter() {
        return interStateTransporter;
    }
}
