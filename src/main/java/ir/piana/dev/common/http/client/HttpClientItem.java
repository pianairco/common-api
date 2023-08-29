package ir.piana.dev.common.http.client;

import lombok.Setter;

@Setter
public class HttpClientItem {
    private String beanName;
    private boolean ssl;
    private String host;
    private int port;
    private int maxPoolSize;

    public HttpClientItem() {
    }

    public String getBeanName() {
        return beanName;
    }

    public boolean isSsl() {
        return ssl;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }
}
