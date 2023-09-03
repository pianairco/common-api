package ir.piana.dev.common.handler;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import ir.piana.dev.common.util.MapStrings;
import ir.piana.dev.jsonparser.json.JsonParser;
import ir.piana.dev.jsonparser.json.JsonTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class HandlerRequestBuilder<Req> {
    @Autowired
    private JsonParser jsonParser;

    public HandlerRequest fromString(String serializedRequest, Class<?> dtoClass, MapStrings mapStrings, String authPhrase) {
        Buffer buffer = Buffer.buffer(serializedRequest);
        return new HandlerRequestImpl(dtoClass != null ? jsonParser.fromJson(buffer.toJsonObject()) : null, buffer, dtoClass, mapStrings, authPhrase);
    }

    public HandlerRequest withoutRequest(MapStrings mapStrings, String authPhrase) {
        JsonTarget jsonTarget = jsonParser.fromJson(JsonObject.of());
        return new HandlerRequestImpl<>(jsonTarget, jsonTarget.getJsonObject().toBuffer(), null, mapStrings, authPhrase);
    }

    public HandlerRequest fromBytes(byte[] serializedRequest, Class dtoClass, MapStrings mapStrings, String authPhrase) {
        Buffer buffer = Buffer.buffer(serializedRequest);
        return new HandlerRequestImpl(dtoClass != null ? jsonParser.fromJson(buffer.toJsonObject()) : jsonParser.fromJson(JsonObject.of()), Buffer.buffer(serializedRequest), dtoClass, mapStrings, authPhrase);
    }

    public HandlerRequest fromJson(JsonObject json, Class dtoClass, MapStrings mapStrings, String authPhrase) {
        return new HandlerRequestImpl(jsonParser.fromJson(json), json.toBuffer(), dtoClass, mapStrings, authPhrase);
    }

    public HandlerRequest fromBuffer(Buffer buffer, Class<?> dtoType, MapStrings mapStrings, String authPhrase) {
        return new HandlerRequestImpl<>(dtoType != null ? jsonParser.fromJson(buffer.toJsonObject()) : jsonParser.fromJson(JsonObject.of()), buffer, dtoType, mapStrings, authPhrase);
    }

    public HandlerRequestCompleter withoutBody() {
        return new HandlerRequestCompleter(new HandlerRequestImpl());
    }

    public class HandlerRequestCompleter {
        private HandlerRequestImpl handlerRequest;

        public HandlerRequestCompleter(HandlerRequestImpl handlerRequest) {
            this.handlerRequest = handlerRequest;
        }

        public HandlerRequestCompleter setAdditionalParam(Consumer<MapStrings.Appender> supplier) {
            MapStrings.Builder consume = MapStrings.toConsume();
            supplier.accept(consume);
            handlerRequest.additionalParams = consume.build();
            return this;
        }

        public HandlerRequestCompleter setAuthPhrase(String authPhrase) {
            handlerRequest.authPhrase = authPhrase;
            return this;
        }

        public HandlerRequest build() {
            return handlerRequest;
        }
    }

    private class HandlerRequestImpl<Req> implements HandlerRequest<Req> {
        private final Buffer buffer;
        private JsonTarget jsonTarget;
        private Req dto;
        private Class<?> dtoType;
        private MapStrings additionalParams;
        private String authPhrase;

//        public HandlerRequestImpl() {
//        }

        //        public HandlerRequestImpl(JsonObject json) {
//            this.json = json;
//        }
        public HandlerRequestImpl(JsonTarget jsonTarget, Buffer buffer, Class<?> dtoType, MapStrings additionalParams, String authPhrase) {
            this.jsonTarget = jsonTarget;
            this.buffer = buffer;
            this.dtoType = dtoType;
            this.additionalParams = additionalParams;
            this.authPhrase = authPhrase;
        }

        public HandlerRequestImpl() {
            this.jsonTarget = jsonParser.fromJson(JsonObject.of());
            this.buffer = jsonTarget.getJsonObject().toBuffer();
        }

        @Override
        public JsonTarget getJsonTarget() {
            if (jsonTarget == null) {
                synchronized (buffer) {
                    if (jsonTarget == null) {
                        jsonTarget = jsonParser.fromJson(buffer.toJsonObject());
                    }
                }
            }
            return jsonTarget;
        }

        @Override
        public String getAuthPhrase() {
            return authPhrase;
        }

        @Override
        public String getSerializedRequest() {
            return buffer.toString();
//            return json == null ? "" : json.toString();
        }

        @Override
        public Req getDto() {
            if (dto == null && dtoType != null) {
                synchronized (buffer) {
                    if (dto == null) {
                        if (dtoType.isAssignableFrom(JsonTarget.class)) dto = (Req) jsonTarget;
                        else dto = (Req) getJsonTarget().mapTo(dtoType);
                    }
                }
            }
            return dto;
        }

        @Override
        public MapStrings getAdditionalParam() {
            return additionalParams;
        }
    }
}
