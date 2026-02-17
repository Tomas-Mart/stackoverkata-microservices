package stackover.gateway.service.dto;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Данные аутентифицированного запроса
 */
@Schema(description = "Данные аутентификации")
public record RequestData(

        @Schema(description = "JWT токен")
        String token,

        @Schema(description = "Данные из токена")
        Claims claims
) {
}