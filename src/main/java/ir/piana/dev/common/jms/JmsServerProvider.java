package ir.piana.dev.common.jms;

import java.util.List;

public interface JmsServerProvider<T> {
    List<JmsServerItem<T>> jmsServers();
}
