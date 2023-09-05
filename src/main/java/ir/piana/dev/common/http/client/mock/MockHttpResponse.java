package ir.piana.dev.common.http.client.mock;

import lombok.Data;

import java.util.Map;

@Data
public class MockHttpResponse {
    private final int status;
    private final String body;
    private final Map<String, String> headers;
}
