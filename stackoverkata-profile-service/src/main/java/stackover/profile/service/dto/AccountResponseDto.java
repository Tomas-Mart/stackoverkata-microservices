package stackover.profile.service.dto;

import stackover.profile.service.enums.RoleNameEnum;

import javax.validation.constraints.NotBlank;

public record AccountResponseDto(long id, @NotBlank String email, RoleNameEnum role, boolean enabled) {
}
