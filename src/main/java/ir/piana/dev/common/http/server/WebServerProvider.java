package ir.piana.dev.common.http.server;

import java.util.List;

public interface WebServerProvider {
    List<HttpServerItem> webServers();
}
