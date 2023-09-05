package ir.piana.dev.common.http.client.mock;

import lombok.Data;

import java.util.List;

@Data
public class MockHttpItem {
    private final String beanName;
    private final List<MockRouteItem> mockRoutes;
}
