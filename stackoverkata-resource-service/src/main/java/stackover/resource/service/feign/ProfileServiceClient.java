package stackover.resource.service.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.ProfileResponseDto;

@Slf4j
@Service
public class ProfileServiceClient {
    private final WebClient webClient;

    public ProfileServiceClient(
            @Value("${app.services.profile.url}") String profileServiceUrl,
            WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(profileServiceUrl)
                .build();
    }

    public Mono<ProfileResponseDto> getProfileByAccountId(Long accountId) {
        log.info("Получение профиля для аккаунта с ID: {}", accountId);
        return webClient.get()
                .uri("/api/inner/profile/{accountId}", accountId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> {
                            log.error("Ошибка при получении профиля для аккаунта {}. Статус: {}",
                                    accountId, response.statusCode());
                            return Mono.error(new RuntimeException(
                                    "Profile service error: " + response.statusCode()));
                        }
                )
                .bodyToMono(ProfileResponseDto.class)
                .onErrorResume(e -> {
                    log.warn("Сервис профилей недоступен. Используем fallback для аккаунта {}. Причина: {}",
                            accountId, e.getMessage());
                    return Mono.just(createFallbackProfile(accountId));
                });
    }

    private ProfileResponseDto createFallbackProfile(Long accountId) {
        log.warn("Создание fallback профиля для аккаунта {}", accountId);
        return new ProfileResponseDto(
                0L, accountId, null, null, null, null,
                null, null, null, null, null, null, null, false
        );
    }
}