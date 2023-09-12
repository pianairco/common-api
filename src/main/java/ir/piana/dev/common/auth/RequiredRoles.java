package ir.piana.dev.common.auth;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RequiredRoles {
    private final boolean allRequired;
    private final List<String> roles;

    public RequiredRoles() {
        this(false, null);
    }

    public RequiredRoles(List<String> roles) {
        this(false, roles);
    }

    public RequiredRoles(boolean allRequired, List<String> roles) {
        this.allRequired = allRequired;
        this.roles = (roles == null || roles.isEmpty()) ? new ArrayList<>() : roles;
    }
}
