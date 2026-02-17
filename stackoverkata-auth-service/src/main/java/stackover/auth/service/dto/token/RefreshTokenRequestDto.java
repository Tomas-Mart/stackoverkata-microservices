package stackover.auth.service.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

/**
 * Запрос на обновление токена
 *
 * @param token Refresh token
 */
@Schema(description = "Запрос на обновление токена")
public record RefreshTokenRequestDto(
        @NotBlank
        @Schema(
                description = "Refresh token",
                example = "KX9pPmTqYtXuWrZv3w6z5C8F1J4H7N2B5E9R0DcGbQa"
        )
        String token
) {
}