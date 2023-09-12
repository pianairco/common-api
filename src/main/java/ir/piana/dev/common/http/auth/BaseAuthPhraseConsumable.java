package ir.piana.dev.common.http.auth;

import ir.piana.dev.common.util.MapAny;

public abstract class BaseAuthPhraseConsumable<T> implements AuthPhraseConsumable<T> {
    protected MapAny configs;

    public BaseAuthPhraseConsumable(MapAny configs) {
        this.configs = configs;
    }
}
