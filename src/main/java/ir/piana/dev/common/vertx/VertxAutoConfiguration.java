package ir.piana.dev.common.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class VertxAutoConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    Vertx vertx(VertxCore core) {
        VertxOptions vertxOptions = new VertxOptions();
        if (core.metrics.enabled) {
            vertxOptions.setMetricsOptions(new MicrometerMetricsOptions()
                    .setEnabled(core.metrics.enabled)
                    .setJvmMetricsEnabled(Boolean.TRUE)
                    .setPrometheusOptions(new VertxPrometheusOptions()
                            .setEnabled(core.metrics.enabled)
                            .setStartEmbeddedServer(Boolean.TRUE)
                            .setEmbeddedServerOptions(new HttpServerOptions()
                                    .setHost(core.metrics.host)
                                    .setPort(core.metrics.port))
                            .setEmbeddedServerEndpoint(core.metrics.endpoint)));
        }
        vertxOptions.setPreferNativeTransport(core.preferNativeTransport);

        return Vertx.vertx(vertxOptions);
    }

    @Setter
    @Component
    @ConfigurationProperties(prefix = "ir.piana.dev.common.vertx")
    static class VertxCore {
        private boolean preferNativeTransport;
        private VertxMetrics metrics;
    }

    @Setter
    static class VertxMetrics {
        private boolean enabled;
        private String host;
        private int port;
        private String endpoint;
    }
}
