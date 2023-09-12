package ir.piana.dev.common.handler;

import ir.piana.dev.common.auth.AnonymousPrincipal;
import ir.piana.dev.common.auth.UserAuthentication;
import ir.piana.dev.common.util.SelfExpiringHashMap;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;

@Component
public class AuthenticationManager {
    private final SelfExpiringHashMap<String, UserAuthentication> selfExpiringHashMap =
            new SelfExpiringHashMap(900_000);

    String add(Serializable principal, UserAuthorization userAuthorization) {
        UUID uuid = UUID.randomUUID();
        selfExpiringHashMap.put(uuid.toString(),
                new UserAuthentication(uuid, principal, userAuthorization));
        return uuid.toString();
    }

    String reassign(String uuid, Serializable principal, UserAuthorization userAuthorization) {

        selfExpiringHashMap.put(uuid,
                new UserAuthentication(UUID.fromString(uuid), principal, userAuthorization));
        return uuid;
    }

    UserAuthentication get(String uuid) {
        UserAuthentication userAuthentication = selfExpiringHashMap.get(uuid);
        if (userAuthentication != null) {
            selfExpiringHashMap.renewKey(uuid);
        } else {
            userAuthentication = new UserAuthentication(
                    UUID.fromString(uuid), new AnonymousPrincipal(), new UserAuthorization());
            selfExpiringHashMap.put(uuid, userAuthentication);
        }
        return userAuthentication;
    }
}
