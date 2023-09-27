package ir.piana.dev.common.auth;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnonymousPrincipal implements Serializable {
    private final String name;
}
