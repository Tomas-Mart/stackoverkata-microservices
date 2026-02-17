package stackover.auth.service.util.enums;

import java.util.Arrays;

public enum RoleNameEnum {
    ROLE_USER,
    ROLE_ADMIN;

    public String getAuthority() {
        return name();
    }

    public static RoleNameEnum fromString(String role) {
        if (role == null) {
            throw new IllegalArgumentException("Название роли не может быть null");
        }

        try {
            return RoleNameEnum.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Недопустимая роль: " + role +
                    ". Допустимые значения: " + Arrays.toString(RoleNameEnum.values()));
        }
    }
}