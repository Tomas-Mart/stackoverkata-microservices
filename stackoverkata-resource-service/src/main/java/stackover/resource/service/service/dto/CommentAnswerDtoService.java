package stackover.resource.service.service.dto;

import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.CommentAnswerResponseDto;

public interface CommentAnswerDtoService {
    /**
     * Добавляет комментарий к ответу и возвращает DTO
     */
    Mono<CommentAnswerResponseDto> addCommentToAnswer(
            Long questionId,
            Long answerId,
            Long accountId,
            String text);

    /**
     * Получает DTO комментария по ID
     */
    Mono<CommentAnswerResponseDto> getCommentAnswerById(Long commentId);
}