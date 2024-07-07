package com.vedha.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Roles {

    USER(
            Set.of(Permissions.USER_READ, Permissions.USER_WRITE)
    ),

    ADMIN(
            Set.of(Permissions.ADMIN_READ, Permissions.ADMIN_WRITE
                    , Permissions.USER_READ, Permissions.USER_WRITE
                    , Permissions.MANAGER_READ, Permissions.MANAGER_WRITE
            )
    ),

    MANAGER(
            Set.of(Permissions.MANAGER_READ, Permissions.MANAGER_WRITE)
    );

    private final Set<Permissions> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>(
                permissions.stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getPermissionValue()))
                        .toList()
        );

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
