package ir.piana.dev.common.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultDto<T> {
    private boolean status;
    private T serializableResponse;
    private DetailedError detailedError;

    public ResultDto(T serializableResponse) {
        this.status = true;
        this.serializableResponse = serializableResponse;
        this.detailedError = null;
    }

    public ResultDto(DetailedError detailedError) {
        this.status = false;
        this.serializableResponse = null;
        this.detailedError = detailedError;
    }

    public boolean isSuccess() {
        return status;
    }

    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public T getSerializableResponse() {
        return serializableResponse;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public DetailedError getErrorContainer() {
        return detailedError;
    }
}
