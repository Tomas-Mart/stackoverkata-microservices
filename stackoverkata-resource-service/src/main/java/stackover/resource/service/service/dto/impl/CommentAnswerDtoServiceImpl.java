package stackover.resource.service.service.dto.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.CommentAnswerResponseDto;
import stackover.resource.service.exception.CommentAnswerException;
import stackover.resource.service.feign.ProfileServiceClient;
import stackover.resource.service.repository.dto.CommentAnswerDtoRepository;
import stackover.resource.service.service.dto.CommentAnswerDtoService;
import stackover.resource.service.service.entity.CommentAnswerService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentAnswerDtoServiceImpl implements CommentAnswerDtoService {

    private final CommentAnswerService commentAnswerService;
    private final ProfileServiceClient profileServiceClient;
    private final CommentAnswerDtoRepository commentAnswerDtoRepository;

    @Override
    public Mono<CommentAnswerResponseDto> addCommentToAnswer(Long questionId, Long answerId, Long accountId, String text) {
        return profileServiceClient.getProfileByAccountId(accountId)
                .flatMap(profile -> commentAnswerService.addCommentToAnswer(answerId, accountId, text)
                        .flatMap(comment -> findSingleCommentAnswer(comment.getId(), null)
                                .doOnSuccess(dto -> log.info("Успешно создан DTO комментария для ответа ID: {}", answerId))
                                .doOnError(e -> log.error("Ошибка при создании DTO комментария: {}", e.getMessage()))));
    }

    @Override
    public Mono<CommentAnswerResponseDto> getCommentAnswerById(Long commentId) {
        return findSingleCommentAnswer(commentId, null)
                .doOnError(e -> log.error("Ошибка при получении DTO комментария ID {}: {}", commentId, e.getMessage()));
    }

    /**
     * Находит один комментарий по ID комментария или ответа
     *
     * @param commentId ID комментария (может быть null)
     * @param answerId  ID ответа (может быть null)
     * @return Mono с DTO комментария или ошибку, если комментарий не найден
     */
    private Mono<CommentAnswerResponseDto> findSingleCommentAnswer(Long commentId, Long answerId) {
        return commentAnswerDtoRepository.findCommentAnswer(commentId, answerId)
                .single()
                .onErrorMap(e -> new CommentAnswerException("Комментарий не найден"));
    }
}