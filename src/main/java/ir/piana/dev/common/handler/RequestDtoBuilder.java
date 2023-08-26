package ir.piana.dev.common.handler;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestDtoBuilder<Req> {

    private RequestDtoImpl<Req> requestDto;

    private RequestDtoBuilder(RequestDtoImpl<Req> requestDto) {
        this.requestDto = requestDto;
    }

    public static RequestDtoBuilder<?> fromString(String serializedRequest) {
        return new RequestDtoBuilder<>(new RequestDtoImpl<>(Buffer.buffer(serializedRequest).toJsonObject()));
    }

    public static RequestDtoBuilder<?> withoutRequest() {
        return new RequestDtoBuilder<>(new RequestDtoImpl<>(Buffer.buffer("{}").toJsonObject()));
    }

    public static RequestDtoBuilder<?> fromBytes(byte[] serializedRequest) {
        return new RequestDtoBuilder<>(new RequestDtoImpl<>(Buffer.buffer(serializedRequest).toJsonObject()));
    }

    public static RequestDtoBuilder<?> fromJson(JsonObject json, Class dtoClass) {
        RequestDtoBuilder requestDtoBuilder = new RequestDtoBuilder<>(new RequestDtoImpl<>(json));
        if (dtoClass != null)
            requestDtoBuilder.requestDto.dto =  json.mapTo(dtoClass);
        return requestDtoBuilder;
    }

    public static RequestDtoBuilder<?> fromBuffer(Buffer buffer) {
        return new RequestDtoBuilder<>(new RequestDtoImpl<>(buffer.toJsonObject()));
    }

    public RequestDtoBuilder<Req> addAdditionalParam(String key, String value) {
        requestDto.additionalParams.put(key, value);
        return this;
    }

    public RequestDtoBuilder<Req> addAdditionalParams(Iterable<Map.Entry<String, String>> iterable) {
        iterable.forEach(i -> requestDto.additionalParams.put(i.getKey(), i.getValue()));
        return this;
    }

    public RequestDto<Req> build() {
        return requestDto;
    }

    private static class RequestDtoImpl<Req> implements RequestDto<Req> {
        private JsonObject json;
        private Req dto;
        private final Map<String, String> additionalParams = new LinkedHashMap<>();

        public RequestDtoImpl() {
        }

        public RequestDtoImpl(JsonObject json) {
            this.json = json;
        }

        @Override
        public JsonObject getJsonObject() {
            return json;
        }

        @Override
        public String getSerializedRequest() {
            return json == null ? "" : json.toString();
        }

        @Override
        public Req getDto() {
            return dto;
        }

        @Override
        public String getAdditionalParam(String key) {
            return additionalParams.get(key);
        }
    }
}
