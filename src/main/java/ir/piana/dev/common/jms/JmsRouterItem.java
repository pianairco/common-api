package ir.piana.dev.common.jms;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@NoArgsConstructor
public class JmsRouterItem {
    private String serverName;
    private List<JmsRouteItem> routes;

    public String getServerName() {
        return serverName;
    }

    public List<JmsRouteItem> getRoutes() {
        return routes;
    }
}
