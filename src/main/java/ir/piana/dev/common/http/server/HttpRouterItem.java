package ir.piana.dev.common.http.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class HttpRouterItem {
    private String serverName;
    private String templateEngineName;
    private List<HttpRouteItem> routes;
    private String authPhraseProviderName;
}
