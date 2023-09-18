package ir.piana.dev.common.http.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Setter
@Getter
public class HttpServerItem {
    private String name;
    private boolean secure;
    private String host;
    private int port;
    /**
     * in seconds
     */
    private int idleTimeout;
    private Map<String, Object> specificConfigs;
    private List<String> routers;
    private String authPhraseProviderName;
    private String templateEngineName;
    private String permissionDeniedPage;
}
