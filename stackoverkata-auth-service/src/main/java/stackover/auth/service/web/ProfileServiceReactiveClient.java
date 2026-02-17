package stackover.auth.service.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.profile.ProfilePostDto;
import stackover.auth.service.dto.profile.ProfileResponseDto;
import stackover.auth.service.exception.FeignRequestException;

@Component
public class ProfileServiceReactiveClient {

    private final WebClient webClient;
    private final String profileServiceUrl;

    public ProfileServiceReactiveClient(WebClient.Builder webClientBuilder,
                                        @Value("${profile.service.url}") String profileServiceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(profileServiceUrl)
                .build();
        this.profileServiceUrl = profileServiceUrl;
    }

    public Mono<Void> createProfile(ProfilePostDto profilePostDto) {
        return webClient.post()
                .uri("/api/inner/profile")
                .bodyValue(profilePostDto)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        Mono.error(new FeignRequestException(
                                String.format("Profile service unavailable. Cannot create profile. Status: %s",
                                        response.statusCode())))
                )
                .toBodilessEntity()
                .then();
    }

    public Mono<ProfileResponseDto> getProfileById(Long accountId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/inner/profile")
                        .queryParam("accountId", accountId)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        Mono.error(new FeignRequestException(
                                String.format("Profile service unavailable. Cannot get profile. Status: %s",
                                        response.statusCode())))
                )
                .bodyToMono(ProfileResponseDto.class);
    }
}