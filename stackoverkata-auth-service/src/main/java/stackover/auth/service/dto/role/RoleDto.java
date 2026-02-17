package stackover.auth.service.dto.role;

import lombok.Builder;
import stackover.auth.service.model.Role;
import stackover.auth.service.util.enums.RoleNameEnum;

@Builder
public record RoleDto(
        Long id,
        RoleNameEnum name
) {

    public RoleDto(Role role) {
        this(
                role.getId(),
                role.getName()
        );
    }
}