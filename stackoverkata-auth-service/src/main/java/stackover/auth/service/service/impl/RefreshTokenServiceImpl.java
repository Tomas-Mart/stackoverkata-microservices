package stackover.auth.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.token.RefreshTokenResponseDto;
import stackover.auth.service.exception.AccountNotAvailableException;
import stackover.auth.service.model.Account;
import stackover.auth.service.model.RefreshToken;
import stackover.auth.service.repository.RefreshTokenRepository;
import stackover.auth.service.security.JwtTokenProvider;
import stackover.auth.service.service.AccountService;
import stackover.auth.service.service.RefreshTokenService;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final AccountService accountService;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Mono<ResponseCookie> generateRefreshTokenCookie(Long accountId) {
        return createRefreshToken(accountId)
                .flatMap(token -> tokenProvider.getRefreshExpirationMs()
                        .map(expirationMs -> ResponseCookie.from("Refresh-Token", token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(expirationMs / 1000)
                                .build()))
                .doOnSuccess(c -> log.debug("Refresh token cookie generated"))
                .doOnError(e -> log.error("Failed to generate refresh token cookie", e));
    }

    @Override
    public Mono<RefreshToken> saveRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken)
                .doOnSuccess(t -> log.debug("Refresh token saved: {}", t.getId()))
                .doOnError(e -> log.error("Failed to save refresh token", e));
    }

    @Override
    public Mono<String> createRefreshToken(Account account) {
        return createRefreshToken(account.getId());
    }

    @Override
    public Mono<String> createRefreshToken(Long accountId) {
        return tokenProvider.getRefreshExpirationMs()
                .flatMap(expirationMs -> {
                    RefreshToken refreshToken = new RefreshToken();
                    refreshToken.setAccountId(accountId);
                    refreshToken.setToken(generateToken());
                    refreshToken.setExpiryDate(Instant.now().plusMillis(expirationMs));

                    return refreshTokenRepository.save(refreshToken)
                            .map(RefreshToken::getToken);
                })
                .onErrorMap(e -> {
                    log.error("Failed to create refresh token for account: {}", accountId, e);
                    return new AccountNotAvailableException("Failed to create refresh token");
                });
    }

    @Override
    public Mono<RefreshToken> createRefreshTokenEntity(RefreshToken refreshToken) {
        return tokenProvider.getRefreshExpirationMs()
                .flatMap(expirationMs -> {
                    refreshToken.setExpiryDate(Instant.now().plusMillis(expirationMs));
                    return refreshTokenRepository.save(refreshToken);
                })
                .doOnSuccess(t -> log.debug("Refresh token entity created: {}", t.getId()))
                .doOnError(e -> log.error("Failed to create refresh token entity", e));
    }

    @Override
    public Mono<RefreshTokenResponseDto> refreshAccessToken(String token) {
        return findByToken(token)
                .flatMap(refreshToken -> validateToken(token)
                        .flatMap(valid -> {
                            if (!valid) {
                                return deleteByToken(token)
                                        .then(Mono.error(new AccountNotAvailableException("Refresh token expired")));
                            }
                            return Mono.just(refreshToken);
                        }))
                .flatMap(validToken -> accountService.getAccountById(validToken.getAccountId())
                        .flatMap(accountDto -> {
                            String roleName = accountDto.role() != null ?
                                    accountDto.role().name() :
                                    "ROLE_USER";
                            return tokenProvider.generateAccessToken(validToken.getAccountId(), roleName)
                                    .map(accessToken -> new RefreshTokenResponseDto(
                                            accessToken,
                                            token,
                                            accountDto.enabled()
                                    ));
                        }))
                .doOnSuccess(dto -> log.debug("Access token refreshed successfully"))
                .doOnError(e -> log.error("Failed to refresh access token", e));
    }

    @Override
    public Mono<RefreshTokenResponseDto> refresh(@NotBlank String token) {
        return refreshAccessToken(token);
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(rt -> rt.getExpiryDate().isAfter(Instant.now()))
                .defaultIfEmpty(false)
                .doOnNext(valid -> {
                    if (!valid) log.warn("Refresh token validation failed");
                });
    }

    @Override
    public Mono<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Refresh token not found");
                    return Mono.error(new AccountNotAvailableException("Refresh token not found"));
                }));
    }

    @Override
    public Mono<Void> deleteByToken(String token) {
        return refreshTokenRepository.deleteByToken(token)
                .doOnSuccess(v -> log.debug("Refresh token deleted"))
                .doOnError(e -> log.error("Failed to delete refresh token", e));
    }

    @Override
    public Mono<Void> deleteByAccountId(Long accountId) {
        return refreshTokenRepository.deleteByAccountId(accountId)
                .doOnSuccess(v -> log.debug("All refresh tokens deleted for account: {}", accountId))
                .doOnError(e -> log.error("Failed to delete refresh tokens for account", e));
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}