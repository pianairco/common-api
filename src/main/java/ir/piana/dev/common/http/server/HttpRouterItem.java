package ir.piana.dev.common.http.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class HttpRouterItem {
    private String name;
    private List<HttpRouteItem> routes;
}
