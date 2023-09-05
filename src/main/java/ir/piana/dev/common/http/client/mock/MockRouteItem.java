package ir.piana.dev.common.http.client.mock;

import lombok.Data;

@Data
public class MockRouteItem {
    private String method;
    private String path;
    private MockHttpResponse response;
}
