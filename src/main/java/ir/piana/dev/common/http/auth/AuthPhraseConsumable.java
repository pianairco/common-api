package ir.piana.dev.common.http.auth;

public interface AuthPhraseConsumable<Req, Res> {
    String consume(Req request);
    void produce(Res request, String authPhrase);
}
