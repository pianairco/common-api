package ir.piana.dev.common.handler;

import ir.piana.dev.jsonparser.json.JsonTarget;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HandlerModelAndViewResponse implements CommonResponse {
    private final String view;
    private final JsonTarget model;
    private final String authPhrase;

    @Override
    public String getAuthPhrase() {
        return authPhrase;
    }
}