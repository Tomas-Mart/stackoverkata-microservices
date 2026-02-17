package stackover.auth.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import stackover.auth.service.model.Account;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@Getter
@Setter
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.access-expiration-ms}")
    private long accessExpirationMs;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public Mono<String> generateAccessToken(Long accountId, String roleName) {
        return Mono.fromCallable(() ->
                Jwts.builder()
                        .setSubject(String.valueOf(accountId))
                        .claim("accountId", accountId)
                        .claim("role", roleName)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                        .signWith(key(), SignatureAlgorithm.HS256)
                        .compact()
        ).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> generateAccessToken(Account account, String roleName) {
        return generateAccessToken(account.getId(), roleName);
    }

    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                log.error("Invalid JWT: {}", e.getMessage());
                return false;
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Claims> getClaimsFromToken(String token) {
        return Mono.fromCallable(() ->
                        Jwts.parserBuilder()
                                .setSigningKey(key())
                                .build()
                                .parseClaimsJws(token)
                                .getBody()
                ).subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(e -> {
                    log.error("Failed to parse JWT claims: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    public Mono<Long> getRefreshExpirationMs() {
        return Mono.just(refreshExpirationMs);
    }

    public Mono<Long> getAccessExpirationSec() {
        return Mono.just(accessExpirationMs / 1000);
    }
}