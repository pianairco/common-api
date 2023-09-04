package ir.piana.dev.common.handler;

import io.vertx.core.buffer.Buffer;
import ir.piana.dev.common.util.MapStrings;
import ir.piana.dev.jsonparser.json.JsonTarget;

public interface HandlerResponse {
    JsonTarget getJsonTarget();
    Buffer getBuffer();
    String getAuthPhrase();
    String getSerializedResponse();
    <Res> Res getDto();
    MapStrings getAdditionalParam();
}
