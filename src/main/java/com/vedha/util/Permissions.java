package com.vedha.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permissions {

    USER_READ("user:read"),

    USER_WRITE("user:write"),

    ADMIN_READ("admin:read"),

    ADMIN_WRITE("admin:write"),

    MANAGER_READ("manager:read"),

    MANAGER_WRITE("manager:write");

    private final String permissionValue;
}
