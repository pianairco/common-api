package ir.piana.dev.common.util;

import io.vertx.core.MultiMap;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapAny {
    boolean modifiableMode;
    private Map<String, Object> map;

    private MapAny() {
        this.modifiableMode = true;
        map = new LinkedHashMap<>();
    }

    private MapAny(Map<String, ? extends Object> map) {
        this.modifiableMode = false;
        this.map = (Map<String, Object>) map;
    }

    public <T> T getValue(String key) {
        return (T) map.get(key);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public boolean put(String key, Object value) {
        if (modifiableMode && !map.containsKey(key))
            return map.put(key, value) != null;
        return false;
    }

    public void putOrThrows(String key, String value) throws PutOnUnmodifiableMapGenericException {
        if (modifiableMode) {
            if (map.containsKey(key)) {
                throw new PutOnUnmodifiableMapGenericException();
            }
            map.put(key, value);
        }
    }

    public static class PutOnUnmodifiableMapGenericException extends Exception {
        public PutOnUnmodifiableMapGenericException() {
            super("not allowed to put on unmodifiable mapGenerics!");
        }
    }

    public static MapAny toProduce() {
        return new MapAny();
    }

    public static MapAny toConsume(Map<String, ? extends Object> map) {
        return new MapAny(map);
    }

    public static Builder toConsume() {
        return new Builder(false);
    }

    public static class Builder extends Appender {
        private boolean modifiableMode;

        private Builder(boolean modifiableMode) {
            super();
            this.modifiableMode = modifiableMode;
        }

        public MapAny build() {
            this.mapAny.modifiableMode = false;
            return this.mapAny;
        }
    }

    public static abstract class Appender {
        protected final MapAny mapAny;

        private Appender() {
            mapAny = new MapAny();
        }

        public abstract MapAny build();

        public Appender putAll(Map<String, Object> map) {
            map.entrySet().forEach(e -> {
                if (!mapAny.containsKey(e.getKey())) {
                    mapAny.map.put(e.getKey(), e.getValue());
                }
            });
            return this;
        }

        public Appender putAll(MultiMap map) {
            map.entries().stream().forEach(e -> {
                if (!mapAny.containsKey(e.getKey())) {
                    mapAny.map.put(e.getKey(),e.getValue());
                }
            });
            return this;
        }

        public Appender putValue(String key, Object value) {
            if (!mapAny.containsKey(key)) {
                mapAny.map.put(key, value);
            }
            return this;
        }
    }
}
