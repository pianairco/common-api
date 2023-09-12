package ir.piana.dev.common.handler;

import ir.piana.dev.common.util.MapAny;
import ir.piana.dev.common.vertx.VertxAutoConfiguration;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@Profile("handler-config")
@Import(VertxAutoConfiguration.class)
public class HandlerConfigAutoConfiguration {
    @Bean
    @Primary
    @Profile("handler-config")
    Map<String, MapAny> mapAnyMap(
            AnnotationConfigApplicationContext applicationContext,
            HandlersConfigs handlersConfigs) {
        final Map<String, MapAny> mapAnyMap = new LinkedHashMap<>();
        if (handlersConfigs.configs == null) {
            mapAnyMap.put("primary", MapAny.toConsume().build());
            return mapAnyMap;
        }
        handlersConfigs.configs.entrySet().forEach(e -> {
            MapAny mapAny = MapAny.toConsume(e.getValue());
            mapAnyMap.put(e.getKey(), mapAny);
            applicationContext.registerBean(e.getKey(), MapAny.class, () -> mapAny);
        });

        return mapAnyMap;
    }

    @Bean
    @Primary
    @Profile("handler-config")
    MapAny primaryMapAny(
            AnnotationConfigApplicationContext applicationContext,
            Map<String, MapAny> mapAnyMap) {
        return mapAnyMap.entrySet().stream().findFirst().get().getValue();
    }

    @Setter
    @Component
    @Profile("handler-config")
    @ConfigurationProperties(prefix = "handler-config")
    static class HandlersConfigs {
        private Map <String, Map<String, Object>> configs;
    }
}
