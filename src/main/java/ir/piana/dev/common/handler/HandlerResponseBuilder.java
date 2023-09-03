package ir.piana.dev.common.handler;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import ir.piana.dev.common.util.MapStrings;
import ir.piana.dev.jsonparser.json.JsonParser;
import ir.piana.dev.jsonparser.json.JsonTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandlerResponseBuilder {
    @Autowired
    private JsonParser jsonParser;

    public <Res> HandlerResponse fromDto(Res responseDto) {
        JsonObject jsonObject = JsonObject.mapFrom(responseDto);
        return new HandlerResponseImpl(jsonObject.toBuffer(), jsonParser.fromJson(jsonObject), responseDto);
    }

    private static class HandlerResponseImpl<Res> implements HandlerResponse<Res> {
        private Buffer buffer;
        private JsonTarget jsonTarget;
        private Res dto;
        private final MapStrings additionalParams = MapStrings.toProduce();
        private String authPhrase;

        public HandlerResponseImpl(Buffer buffer, JsonTarget jsonTarget) {
            this.buffer = buffer;
            this.jsonTarget = jsonTarget;
        }

        public HandlerResponseImpl(Buffer buffer, JsonTarget jsonTarget, Res dto) {
            this.buffer = buffer;
            this.jsonTarget = jsonTarget;
            this.dto = dto;
        }

        @Override
        public JsonTarget getJsonTarget() {
            return jsonTarget;
        }

        public Buffer getBuffer() {
            return buffer;
        }

        @Override
        public String getSerializedResponse() {
            return buffer.toString();
        }

        @Override
        public Res getDto() {
            return dto;
        }

        @Override
        public MapStrings getAdditionalParam() {
            return additionalParams;
        }

        @Override
        public String getAuthPhrase() {
            return authPhrase;
        }
    }
}
