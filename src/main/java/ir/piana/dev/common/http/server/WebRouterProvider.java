package ir.piana.dev.common.http.server;

import java.util.List;

public interface WebRouterProvider {
    List<HttpRouterItem> webRouters();
}
