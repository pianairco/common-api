package ir.piana.dev.common.jms;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class JmsServerItem<T> {
    private String name;
    private String type;
    private String serverUrl;
    /**
     * in seconds
     */
    private T specificParams;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public T getSpecificParams() {
        return specificParams;
    }
}
