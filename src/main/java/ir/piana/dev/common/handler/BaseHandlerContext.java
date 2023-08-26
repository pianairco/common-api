package ir.piana.dev.common.handler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaseHandlerContext<Req> implements HandlerContext<Req> {
    private final AtomicBoolean responded;
    private final String handlerName;
    private final String uniqueId;
    private Map<String, Object> map;
    private ResultDto resultDto;
    private RequestDto<Req> requestDto;

    public BaseHandlerContext(String handlerName, String uniqueId, RequestDto<Req> requestDto) {
        this.handlerName = handlerName;
        this.uniqueId = uniqueId;
        this.requestDto = requestDto;
        this.responded = new AtomicBoolean(false);
        map = new LinkedHashMap<>();
    }

    /*public static HandlerContext<?> fromRequest(RequestDto<?> requestDto) {
        return new BaseHandlerContext<>(requestDto);
    }

    public static HandlerContext<?> fromRequest(String handlerBeanName, String uniqueId, RequestDto<?> requestDto) {
        return new BaseHandlerContext<>(requestDto).uniqueId;
    }*/

    public static HandlerContext<?> create(String handlerName, String uniqueId, RequestDto<?> requestDto) {
        return new BaseHandlerContext<>(handlerName, uniqueId, requestDto);
    }

    @Override
    public boolean handlerName() {
        return false;
    }

    @Override
    public boolean responded() {
        return responded.getAndSet(true);
    }

    @Override
    public RequestDto<Req> requestDto() {
        return requestDto;
    }

    @Override
    public HandlerContext addResultDto(ResultDto resultDto) {
        this.resultDto = resultDto;
        return this;
    }

    @Override
    public ResultDto resultDto() {
        return resultDto;
    }

    @Override
    public <T> HandlerContext put(String key, T val) {
        map.put(key, val);
        return this;
    }

    @Override
    public <T> T get(String key) {
        return (T) map.get(key);
    }

    @Override
    public String uniqueId() {
        return "";
    }
}
