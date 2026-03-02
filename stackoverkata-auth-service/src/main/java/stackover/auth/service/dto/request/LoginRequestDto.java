package stackover.auth.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import stackover.auth.service.model.Account;

import jakarta.validation.constraints.Email;

/**
 * DTO для запроса входа в систему
 *
 * @param email    Email пользователя
 * @param password Пароль пользователя
 */

@Builder
@Schema(description = "DTO для запроса входа")
public record LoginRequestDto(
        @Email @Schema(description = "Email пользователя") String email,
        @Schema(description = "Пароль") String password
) {
    /**
     * Статический фабричный метод для создания из Account (если нужно)
     *
     * @param account Сущность аккаунта
     * @return LoginRequestDto
     */
    public static LoginRequestDto fromAccount(Account account) {
        return new LoginRequestDto(account.getEmail(), null); // Пароль не копируем из соображений безопасности
    }
}