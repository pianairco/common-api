package ir.piana.dev.common.http.auth;

public interface AuthPhraseConsumable<T> {
    String consume(T request);
}
