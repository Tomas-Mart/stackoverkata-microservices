package stackover.resource.service.service.dto.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.TagResponseDto;
import stackover.resource.service.service.dto.TagDtoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagDtoServiceImpl implements TagDtoService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<List<TagResponseDto>> findTagsByQuestionId(Long questionId) {
        return webClientBuilder.build().get()
                .uri("/api/tags/question/{questionId}", questionId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("Question not found with id: " + questionId))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Tag service unavailable"))
                )
                .bodyToFlux(TagResponseDto.class)
                .collectList();
    }

    @Override
    public Mono<List<TagResponseDto>> getTop3TagsByUserId(Long userId) {
        return webClientBuilder.build().get()
                .uri("/api/tags/user/{userId}/top3", userId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("User not found with id: " + userId))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Tag service unavailable"))
                )
                .bodyToFlux(TagResponseDto.class)
                .collectList();
    }
}