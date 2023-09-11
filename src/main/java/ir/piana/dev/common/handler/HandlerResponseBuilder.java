package ir.piana.dev.common.handler;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import ir.piana.dev.common.util.MapStrings;
import ir.piana.dev.jsonparser.json.JsonParser;
import ir.piana.dev.jsonparser.json.JsonTarget;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.function.Consumer;

@Component
public class HandlerResponseBuilder {
    @Autowired
    private JsonParser jsonParser;

    public HandlerResponseCompleter fromBuffer(Buffer buffer) {
        return new HandlerResponseCompleter(new HandlerResponseImpl(
                buffer, null));
    }

    public HandlerResponseCompleter fromJsonTarget(JsonTarget jsonTarget) {
        return new HandlerResponseCompleter(new HandlerResponseImpl(
                jsonTarget.getJsonObject().toBuffer(), jsonTarget));
    }

    public <Res> HandlerResponseCompleter fromJsonTarget(JsonTarget jsonTarget, Class<Res> dtoType) {
        return new HandlerResponseCompleter(new HandlerResponseImpl<Res>(
                jsonTarget.getJsonObject().toBuffer(), jsonTarget, jsonTarget.mapTo(dtoType)));
    }

    public <Res> HandlerResponseCompleter fromDto(Res responseDto) {
        JsonObject jsonObject = JsonObject.mapFrom(responseDto);
        return new HandlerResponseCompleter(new HandlerResponseImpl<Res>(
                jsonObject.toBuffer(), jsonParser.fromJson(jsonObject), responseDto));
    }

    public <Res> HandlerResponseCompleter withoutBody() {
        JsonObject jsonObject = JsonObject.of();
        return new HandlerResponseCompleter(new HandlerResponseImpl<Res>(
                jsonObject.toBuffer(), jsonParser.fromJson(jsonObject), (Res) new Object()));
    }

    public class HandlerResponseCompleter {
        private HandlerResponseBuilder.HandlerResponseImpl handlerResponse;

        public HandlerResponseCompleter(HandlerResponseBuilder.HandlerResponseImpl handlerResponse) {
            this.handlerResponse = handlerResponse;
        }

        public HandlerResponseBuilder.HandlerResponseCompleter setAdditionalParam(
                Consumer<MapStrings.Appender> consumer) {
            MapStrings.Builder consume = MapStrings.toConsume();
            consumer.accept(consume);
            handlerResponse.additionalParams = consume.build();
            return this;
        }

        public HandlerResponseBuilder.HandlerResponseCompleter setAuthPhrase(
                String authPhrase) {
            handlerResponse.authPhrase = authPhrase;
            return this;
        }

        public HandlerResponse build() {
            return handlerResponse;
        }
    }

    private static class HandlerResponseImpl<Res> implements HandlerResponse<Res> {
        @Getter
        private Buffer buffer;
        private JsonTarget jsonTarget;
        private Object dto;
        private MapStrings additionalParams = MapStrings.toProduce();
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

        @Override
        public String getSerializedResponse() {
            return buffer.toString();
        }

        @Override
        public Res getDto() {
            return (Res) dto;
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
