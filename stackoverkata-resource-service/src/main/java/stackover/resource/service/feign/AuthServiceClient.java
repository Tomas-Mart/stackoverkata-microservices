package stackover.resource.service.feign;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.UserResponseDto;

import javax.naming.ServiceUnavailableException;

@Slf4j
@Service
public class AuthServiceClient {
    private final WebClient webClient;

    public AuthServiceClient(
            @Value("${app.services.auth.url}") String authServiceUrl,
            WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(authServiceUrl)
                .build();
    }

    public Mono<Boolean> isAccountExist(Long accountId) {
        log.info("Проверка существования аккаунта с ID: {}", accountId);
        return webClient.get()
                .uri("/api/internal/account/{accountId}/exists", accountId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> {
                            log.error("Аккаунт с ID {} не найден", accountId);
                            return Mono.error(new EntityNotFoundException("Аккаунт не найден"));
                        }
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> {
                            log.error("Сервис авторизации недоступен в isAccountExist()");
                            return Mono.error(new ServiceUnavailableException("Сервис авторизации недоступен"));
                        }
                )
                .bodyToMono(Boolean.class)
                .onErrorResume(e -> {
                    log.error("Ошибка при проверке существования аккаунта: {}", e.getMessage());
                    return Mono.error(new RuntimeException(
                            "Не удалось проверить существование аккаунта: " + e.getMessage()));
                });
    }

    public Mono<UserResponseDto> getUserById(Long accountId) {
        log.info("Получение пользователя по ID: {}", accountId);
        return webClient.get()
                .uri("/api/users/{id}", accountId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> {
                            log.error("Пользователь с ID {} не найден", accountId);
                            return Mono.error(new EntityNotFoundException("Пользователь не найден"));
                        }
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> {
                            log.error("Сервис авторизации недоступен в getUserById()");
                            return Mono.error(new ServiceUnavailableException("Сервис авторизации недоступен"));
                        }
                )
                .bodyToMono(UserResponseDto.class)
                .onErrorResume(e -> {
                    log.error("Ошибка при получении пользователя: {}", e.getMessage());
                    return Mono.error(e);
                });
    }
}