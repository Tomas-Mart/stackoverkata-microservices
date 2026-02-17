package stackover.auth.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import reactor.core.publisher.Mono;
import stackover.auth.service.model.Account;
import stackover.auth.service.security.JwtTokenProvider;

/**
 * DTO для ответа аутентификации
 */
@Builder
@Schema(description = "DTO для ответа аутентификации")
public record AuthResponseDto(
        @Schema(description = "ID аккаунта") Long accountId,
        @Schema(description = "Email") String email,
        @Schema(description = "JWT токен") String accessToken,
        @Schema(description = "Время жизни токена (сек)") Long expiresIn
) {

    /**
     * Реактивный фабричный метод для создания AuthResponseDto
     */
    public static Mono<AuthResponseDto> createReactive(
            Account account,
            JwtTokenProvider tokenProvider,
            String roleName
    ) {
        return Mono.defer(() ->
                tokenProvider.generateAccessToken(account, roleName)
                        .zipWith(tokenProvider.getAccessExpirationSec())
                        .map(tuple -> AuthResponseDto.builder()
                                .accountId(account.getId())
                                .email(account.getEmail())
                                .accessToken(tuple.getT1())
                                .expiresIn(tuple.getT2())
                                .build()
                        )
        );
    }

    /**
     * Альтернативный метод для использования с профилем (если email хранится в профиле)
     */
    public static Mono<AuthResponseDto> createWithProfile(
            Account account,
            String email,
            JwtTokenProvider tokenProvider,
            String roleName
    ) {
        return Mono.defer(() ->
                tokenProvider.generateAccessToken(account, roleName)
                        .zipWith(tokenProvider.getAccessExpirationSec())
                        .map(tuple -> AuthResponseDto.builder()
                                .accountId(account.getId())
                                .email(email)
                                .accessToken(tuple.getT1())
                                .expiresIn(tuple.getT2())
                                .build()
                        )
        );
    }
}