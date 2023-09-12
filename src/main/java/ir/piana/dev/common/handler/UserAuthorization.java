package ir.piana.dev.common.handler;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UserAuthorization implements Serializable {
    private final HashSet<String> roles;

    UserAuthorization() {
        this.roles = new HashSet<>(List.of("anonymous"));
    }

    public UserAuthorization(String role, String... otherRoles) {
        this(Stream.concat(Arrays.stream(otherRoles), Stream.of(role)).toList());
    }

    public UserAuthorization(List<String> roles) {
        roles = Optional.ofNullable(roles).orElse(List.of("authenticated")).stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(r -> !r.isEmpty())
                .filter(r -> !r.equalsIgnoreCase("anonymous"))
                .collect(Collectors.toList());
        if (!roles.contains("authenticated"))
            roles.add("authenticated");
        this.roles = new HashSet<>(roles);
    }

    public Set<String> roles() {
        return roles;
    }
    public boolean isAuthorized(String role) {
        return roles.contains(role);
    }

    public boolean hasAnyRole(List<String> roles) {
        return roles == null || roles.isEmpty() || this.roles.stream().anyMatch(roles::contains);
    }

    /*public boolean hasAnyRole(String... roles) {
        return Arrays.stream(Optional.ofNullable(roles).orElse(new String[0]))
                .anyMatch(this.roles::contains);
    }*/

    /*ToDo anonymous should be check (resource should have only anonymous role or any role except anonymous)*/
    public boolean hasAllRole(List<String> roles) {
        return roles == null || roles.isEmpty() || this.roles.containsAll(roles);
    }

    /*public boolean hasAllRole(String... roles) {
        return this.roles.containsAll(
                Arrays.stream(Optional.ofNullable(roles).orElse(new String[0]))
                        .toList()
        );
    }*/
}
