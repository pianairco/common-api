package ir.piana.dev.common.handler;

public interface HandlerContext<Req> {
    boolean handlerName();
    boolean responded();
    String uniqueId();
    RequestDto<Req> requestDto();
    HandlerContext addResultDto(ResultDto resultDto);
    ResultDto resultDto();
    <T> HandlerContext put(String key, T val);
    <T> T get(String key);
}
