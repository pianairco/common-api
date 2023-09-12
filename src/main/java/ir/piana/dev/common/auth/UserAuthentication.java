package ir.piana.dev.common.auth;

import ir.piana.dev.common.handler.UserAuthorization;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter(AccessLevel.PACKAGE)
public final class UserAuthentication implements Serializable {
    private final UUID sessionId;
    private final Serializable principal;
    private final UserAuthorization userAuthorization;
}
