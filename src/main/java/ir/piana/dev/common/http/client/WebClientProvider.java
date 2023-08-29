package ir.piana.dev.common.http.client;

import java.util.List;

public interface WebClientProvider {
    List<HttpClientItem> webClients();
}
