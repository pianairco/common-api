package ir.piana.dev.common.handler;

import java.util.*;
import java.util.stream.Collectors;

public class HandlerInterStateTransporter {
    public static final List<String> defaultScopes = Arrays.asList(
            "handler", "base", "common", "security", "internal");
    private Map<String, Map<String, Object>> scopeMap;

    HandlerInterStateTransporter() {
        scopeMap = new LinkedHashMap<>();
        scopeMap.put(null, new LinkedHashMap<>());
    }

    public <T> T getValue(String key) {
        return (T) Optional.ofNullable(scopeMap.get(null).get(key)).orElse(null);
    }

    public boolean containsKey(String key) {
        return scopeMap.get(null).containsKey(key);
    }

    public <T> boolean put(String key, T value) {
        if (!scopeMap.get(null).containsKey(key))
            return scopeMap.get(null).put(key, value) != null;
        return false;
    }

    public <T> void putOrThrows(String key, T value) throws DuplicatePutException {
        if (scopeMap.get(null).containsKey(key))
            throw new DuplicatePutException();
        scopeMap.get(null).put(key, value);
    }

    public static class DuplicatePutException extends Exception {
        public DuplicatePutException() {
            super("not allowed to put on by an existing key!!");
        }
    }

    public enum DefaultInterstateScopes {
        handler, base, common, security, internal;

        public static List<String> getNames() {
            return Arrays.stream(DefaultInterstateScopes.values())
                    .map(Object::toString).collect(Collectors.toList());
        }
    }
}
