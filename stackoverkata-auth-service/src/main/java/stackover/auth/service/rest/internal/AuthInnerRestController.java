package stackover.auth.service.rest.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import stackover.auth.service.security.JwtTokenProvider;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/auth")
public class AuthInnerRestController {

    private final JwtTokenProvider tokenProvider;

    @GetMapping("/validate")
    public Mono<ResponseEntity<Boolean>> validateToken(@RequestHeader("Authorization") String authHeader) {
        return Mono.justOrEmpty(authHeader)
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .flatMap(token -> tokenProvider.validateToken(token)
                        .map(ResponseEntity::ok)
                        .defaultIfEmpty(ResponseEntity.ok(false))
                        .onErrorResume(e -> {
                                    log.error("Token validation error", e);
                                    return Mono.just(ResponseEntity.internalServerError().body(false));
                                }
                        )
                );
    }
}