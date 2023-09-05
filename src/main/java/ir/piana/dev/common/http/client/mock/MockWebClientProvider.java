package ir.piana.dev.common.http.client.mock;

import java.util.List;

public interface MockWebClientProvider {
    List<MockHttpItem> mocks();
}
