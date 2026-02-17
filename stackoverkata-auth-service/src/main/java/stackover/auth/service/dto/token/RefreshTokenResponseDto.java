package stackover.auth.service.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "DTO для ответа с обновленными токенами")
public record RefreshTokenResponseDto(

        @Schema(description = "Новый access token")
        String accessToken,

        @Schema(description = "Новый refresh token")
        String refreshToken,

        @Schema(description = "Флаг блокировки токена")
        boolean blocked
) {
}
