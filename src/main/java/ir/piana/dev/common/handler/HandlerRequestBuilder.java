package ir.piana.dev.common.handler;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import ir.piana.dev.jsonparser.json.JsonParser;
import ir.piana.dev.jsonparser.json.JsonTarget;

import java.util.LinkedHashMap;
import java.util.Map;

public class HandlerRequestBuilder<Req> {

    private HandlerRequestImpl<Req> requestDto;

    private HandlerRequestBuilder(HandlerRequestImpl<Req> requestDto) {
        this.requestDto = requestDto;
    }

    public static HandlerRequestBuilder<?> fromString(JsonParser jsonParser, String serializedRequest, Class<?> dtoClass) {
        return new HandlerRequestBuilder<>(new HandlerRequestImpl<>(jsonParser, Buffer.buffer(serializedRequest), dtoClass));
    }

    public static HandlerRequestBuilder<?> withoutRequest() {
        return new HandlerRequestBuilder<>(new HandlerRequestImpl<>(null, Buffer.buffer("{}"), null));
    }

    public static HandlerRequestBuilder<?> fromBytes(JsonParser jsonParser, byte[] serializedRequest, Class dtoClass) {
        return new HandlerRequestBuilder<>(new HandlerRequestImpl<>(jsonParser, Buffer.buffer(serializedRequest), dtoClass));
    }

    public static HandlerRequestBuilder<?> fromJson(JsonParser jsonParser, JsonObject json, Class dtoClass) {
        HandlerRequestBuilder handlerRequestBuilder = new HandlerRequestBuilder<>(
                new HandlerRequestImpl<>(jsonParser, json.toBuffer(), dtoClass));
        return handlerRequestBuilder;
    }

    public static HandlerRequestBuilder<?> fromBuffer(JsonParser jsonParser, Buffer buffer, Class<?> dtoType) {
        return new HandlerRequestBuilder<>(new HandlerRequestImpl<>(jsonParser, buffer, dtoType));
    }

    public HandlerRequestBuilder<Req> addAdditionalParam(String key, String value) {
        requestDto.additionalParams.put(key, value);
        return this;
    }

    public HandlerRequestBuilder<Req> addAdditionalParams(Iterable<Map.Entry<String, String>> iterable) {
        iterable.forEach(i -> requestDto.additionalParams.put(i.getKey(), i.getValue()));
        return this;
    }

    public HandlerRequest<Req> build() {
        return requestDto;
    }

    private static class HandlerRequestImpl<Req> implements HandlerRequest<Req> {
        private final JsonParser jsonParser;
        private final Buffer buffer;
        private JsonTarget json;
        private Req dto;

        private Class<?> dtoType;

        private final Map<String, String> additionalParams = new LinkedHashMap<>();

//        public HandlerRequestImpl() {
//        }

//        public HandlerRequestImpl(JsonObject json) {
//            this.json = json;
//        }
        public HandlerRequestImpl(JsonParser jsonParser, Buffer buffer, Class<?> dtoType) {
            this.jsonParser = jsonParser;
            this.buffer = buffer;
            this.dtoType = dtoType;
        }

        @Override
        public JsonTarget getJsonTarget() {
            if (json == null) {
                synchronized (buffer) {
                    if (json == null) {
                        json = jsonParser.fromJson(buffer.toJsonObject());
                    }
                }
            }
            return json;
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
                        if (dtoType.isAssignableFrom(JsonTarget.class))
                            dto = (Req) json;
                        else
                            dto = (Req) getJsonTarget().mapTo(dtoType);
                    }
                }
            }
            return dto;
        }

        @Override
        public String getAdditionalParam(String key) {
            return additionalParams.get(key);
        }
    }
}
