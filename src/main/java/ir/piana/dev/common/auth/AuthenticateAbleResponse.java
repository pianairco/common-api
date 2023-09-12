package ir.piana.dev.common.auth;

import ir.piana.dev.common.handler.UserAuthorization;
import ir.piana.dev.jsonparser.json.JsonTarget;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class AuthenticateAbleResponse {
    private String view;
    private JsonTarget model;
    private final Serializable principal;
    private final UserAuthorization userAuthorization;
    private final boolean isAuthenticated;
}
