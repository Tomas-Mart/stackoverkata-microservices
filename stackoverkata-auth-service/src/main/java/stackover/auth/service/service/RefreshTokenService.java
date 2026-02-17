package stackover.auth.service.service;

import org.springframework.http.ResponseCookie;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.token.RefreshTokenResponseDto;
import stackover.auth.service.model.Account;
import stackover.auth.service.model.RefreshToken;

import javax.validation.constraints.NotBlank;

public interface RefreshTokenService {

    Mono<ResponseCookie> generateRefreshTokenCookie(Long accountId);

    Mono<RefreshToken> saveRefreshToken(RefreshToken refreshToken);

    Mono<String> createRefreshToken(Account account);  // Оставляем для обратной совместимости

    // Добавляем новый метод
    Mono<String> createRefreshToken(Long accountId);

    Mono<RefreshToken> createRefreshTokenEntity(RefreshToken refreshToken);

    Mono<RefreshTokenResponseDto> refreshAccessToken(@NotBlank String token);

    Mono<RefreshTokenResponseDto> refresh(@NotBlank String token);

    Mono<Boolean> validateToken(String token);

    Mono<Void> deleteByAccountId(Long accountId);

    Mono<RefreshToken> findByToken(String token);

    Mono<Void> deleteByToken(String token);
}