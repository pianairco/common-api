package ir.piana.dev.common.http.auth;

import ir.piana.dev.common.util.MapAny;

public abstract class BaseAuthPhraseConsumable<Req, Res> implements AuthPhraseConsumable<Req, Res> {
    protected MapAny configs;

    public BaseAuthPhraseConsumable(MapAny configs) {
        this.configs = configs;
    }
}
