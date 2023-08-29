package ir.piana.dev.common.http.server;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class HttpServerItem {
    private String name;
    private String host;
    private int port;
    /**
     * in seconds
     */
    private int idleTimeout;

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }
}
