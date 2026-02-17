package stackover.gateway.service.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class AuthServiceWebClient {

    private static final String AUTH_SERVICE_NAME = "lb://stackover-auth-service";
    private static final String VALIDATE_ENDPOINT = "/api/internal/auth/validate";
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final Duration RETRY_BACKOFF = Duration.ofMillis(500);

    private final WebClient webClient;

    public AuthServiceWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(AUTH_SERVICE_NAME)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    public Mono<Boolean> validateToken(String authorizationToken) {
        return webClient.get()
                .uri(VALIDATE_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, authorizationToken)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.UNAUTHORIZED,
                        response -> Mono.error(new SecurityException("Invalid authorization token"))
                )
                .bodyToMono(Boolean.class)
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, RETRY_BACKOFF)
                        .filter(this::isRetryableException))
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> Mono.just(false));
    }

    private boolean isRetryableException(Throwable throwable) {
        return !(throwable instanceof SecurityException);
    }
}