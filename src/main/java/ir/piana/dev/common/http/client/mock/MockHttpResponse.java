package ir.piana.dev.common.http.client.mock;

import io.vertx.core.buffer.Buffer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class MockHttpResponse {
    private int status;
    @Setter(AccessLevel.NONE)
    private String body;
    @Setter(AccessLevel.NONE)
    private Buffer bodyAsBuffer;
    private Map<String, String> headers;

    public void setBody(String body) {
        this.body = body;
        this.bodyAsBuffer = Buffer.buffer(body);
    }

    public MockHttpResponse() {

    }

    public MockHttpResponse(int status, String body, Map<String, String> headers) {
        this.status = status;
        this.body = body;
        this.bodyAsBuffer = Buffer.buffer(body);
        this.headers = headers;
    }

    public MockHttpResponse(int status, Buffer bodyAsBuffer, Map<String, String> headers) {
        this.status = status;
        this.body = bodyAsBuffer.toString();
        this.bodyAsBuffer = bodyAsBuffer;
        this.headers = headers;
    }
}

