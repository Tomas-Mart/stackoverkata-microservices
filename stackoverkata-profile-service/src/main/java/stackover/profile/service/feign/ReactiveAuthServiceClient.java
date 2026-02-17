package stackover.profile.service.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import stackover.profile.service.dto.AccountResponseDto;
import stackover.profile.service.exception.AccountExistException;

@Component
public class ReactiveAuthServiceClient {

    private final WebClient webClient;
    private final String serviceUrl;

    public ReactiveAuthServiceClient(WebClient.Builder webClientBuilder) {
        this.serviceUrl = "http://stackover-auth-service/api/internal/account";
        this.webClient = webClientBuilder.baseUrl(serviceUrl).build();
    }

    public Mono<ResponseEntity<AccountResponseDto>> getAccountById(long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .toEntity(AccountResponseDto.class)
                .onErrorResume(e -> Mono.just(createFallbackResponse(id)));
    }

    private ResponseEntity<AccountResponseDto> createFallbackResponse(long id) {
        String responseMessage = String.format("Account with id %s not found", id);
        throw new AccountExistException(responseMessage);
    }
}