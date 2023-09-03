package ir.piana.dev.common.util;

import io.vertx.core.MultiMap;

import java.util.*;

public class MapStrings {
    List<String> empty = Collections.emptyList();

    boolean modifiableMode;
    private Map<String, List<String>> map;

    private MapStrings() {
        this.modifiableMode = true;
        map = new LinkedHashMap<>();
    }

    private MapStrings(Map<String, List<String>> map) {
        this.modifiableMode = false;
        this.map = map;
    }

    public String getValueByIndex(String key, int index) {
        return Optional.ofNullable(map.get(key))
                .map(l -> {
                    return l.size() > index ? l.get(index) : null;
                })
                .orElse(null);
    }

    public String getFirstValue(String key) {
        return Optional.ofNullable(map.get(key)).map(l -> l.get(0)).orElse(null);
    }

    public List<String> getValues(String key) {
        return Optional.ofNullable(map.get(key)).orElse(empty);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public boolean put(String key, String value) {
        if (modifiableMode)
            return Optional.ofNullable(map.get(key))
                    .orElseGet(() -> map.put(key, Arrays.asList(value))) != null;
        return false;
    }

    public void putOrThrows(String key, String value) throws PutOnUnmodifiableMapStringsException {
        if (modifiableMode)
            Optional.ofNullable(map.get(key))
                    .orElseGet(() -> map.put(key, Arrays.asList(value)));
        throw new PutOnUnmodifiableMapStringsException();
    }

    public static class PutOnUnmodifiableMapStringsException extends Exception {
        public PutOnUnmodifiableMapStringsException() {
            super("not allowed to put on unmodifiable mapStrings!");
        }
    }

    public static MapStrings toProduce () {
        return new MapStrings();
    }

    public static MapStrings toConsume (Map<String, List<String>> map) {
        return new MapStrings(map);
    }

    public static Builder toConsume () {
        return new Builder(false);
    }

    public static class Builder extends Appender {
        private boolean modifiableMode;
        private Builder(boolean modifiableMode) {
            super();
            this.modifiableMode = modifiableMode;
        }

        public MapStrings build() {
            this.mapStrings.modifiableMode = false;
            return this.mapStrings;
        }
    }

    public static abstract class Appender {
        protected final MapStrings mapStrings;
        private Appender() {
            mapStrings = new MapStrings();
        }

        public abstract MapStrings build();

        public Appender putAll(Map<String, List<String>> map) {
            map.entrySet().forEach(e -> {
                if (!mapStrings.containsKey(e.getKey())) {
                    mapStrings.map.put(e.getKey(), e.getValue());
                } else {
                    mapStrings.map.get(e.getKey()).addAll(e.getValue());
                }
            });
            return this;
        }

        public Appender putAll(MultiMap map) {
            map.entries().stream().forEach(e -> {
                if (!mapStrings.containsKey(e.getKey())) {
                    mapStrings.map.put(e.getKey(), Arrays.asList(e.getValue()));
                } else {
                    mapStrings.map.get(e.getKey()).add(e.getValue());
                }
            });
            return this;
        }

        public Appender putAllOneValue(Map<String, String> map) {
            map.entrySet().forEach(e -> {
                if (!mapStrings.containsKey(e.getKey())) {
                    mapStrings.map.put(e.getKey(), Arrays.asList(e.getValue()));
                } else {
                    mapStrings.map.get(e.getKey()).add(e.getValue());
                }
            });
            return this;
        }

        public Appender putValue(String key, String value) {
            if (!mapStrings.containsKey(key)) {
                mapStrings.map.put(key, Arrays.asList(value));
            } else {
                mapStrings.map.get(key).add(value);
            }
            return this;
        }

        public Appender putValues(String key, List<String> values) {
            if (!mapStrings.containsKey(key)) {
                mapStrings.map.put(key, values);
            } else {
                mapStrings.map.get(key).addAll(values);
            }
            return this;
        }
    }
}
