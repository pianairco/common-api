package ir.piana.dev.common.handler;

import io.vertx.core.json.JsonObject;

public interface RequestDto<Req> {
    JsonObject getJsonObject();
    String getSerializedRequest();
    Req getDto();
    String getAdditionalParam(String key);
}
