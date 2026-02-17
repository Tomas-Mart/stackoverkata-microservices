package stackover.auth.service.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import stackover.auth.service.model.Account;
import stackover.auth.service.util.enums.RoleNameEnum;

import jakarta.validation.constraints.Email;

/**
 * DTO для ответа с данными аккаунта
 */
@Builder
@Schema(description = "DTO для ответа с данными аккаунта")
public record AccountResponseDto(
        @Schema(description = "ID аккаунта") Long id,
        @Email @Schema(description = "Email пользователя") String email,
        @Schema(description = "Роль пользователя") RoleNameEnum role,
        @Schema(description = "Статус аккаунта") boolean enabled
) {

    /**
     * Конструктор для создания DTO из Account с указанием роли
     */
    public AccountResponseDto(Account account, RoleNameEnum role) {
        this(
                account.getId(),
                account.getEmail(),
                role,
                account.getEnabled()
        );
    }

    /**
     * Конструктор для создания DTO из Account без указания роли (будет null)
     */
    public AccountResponseDto(Account account) {
        this(account, null);
    }
}