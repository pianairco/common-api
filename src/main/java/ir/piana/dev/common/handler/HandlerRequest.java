package ir.piana.dev.common.handler;

import ir.piana.dev.jsonparser.json.JsonTarget;

public interface HandlerRequest<Req> {
    JsonTarget getJsonTarget();
    String getSerializedRequest();
    Req getDto();
    String getAdditionalParam(String key);
}
