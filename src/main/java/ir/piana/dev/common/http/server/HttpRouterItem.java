package ir.piana.dev.common.http.server;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@NoArgsConstructor
public class HttpRouterItem {
    private String serverName;
    private List<HttpRouteItem> routes;

    public String getServerName() {
        return serverName;
    }

    public List<HttpRouteItem> getRoutes() {
        return routes;
    }
}
