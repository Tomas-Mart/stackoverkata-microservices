package stackover.resource.service.service.dto.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import stackover.resource.service.dto.response.CommentQuestionResponseDto;
import stackover.resource.service.feign.AuthServiceClient;
import stackover.resource.service.repository.dto.CommentDtoRepository;
import stackover.resource.service.service.dto.CommentDtoService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentDtoServiceImpl implements CommentDtoService {

    private final AuthServiceClient authServiceClient;
    private final CommentDtoRepository commentDtoRepository;

    @Override
    public Flux<CommentQuestionResponseDto> getAllCommentsOnQuestion(Long questionId, Long accountId) {
        log.debug("Начало получения комментариев для вопроса ID: {}, аккаунт ID: {}", questionId, accountId);

        return authServiceClient.isAccountExist(accountId)
                .flatMapMany(accountExists -> {
                    if (!accountExists) {
                        log.warn("Аккаунт с ID {} не существует", accountId);
                        return Flux.error(new RuntimeException("Аккаунт не найден"));
                    }
                    log.debug("Аккаунт существует, запрашиваем комментарии...");
                    return commentDtoRepository.getAllQuestionCommentDtoById(questionId)
                            .switchIfEmpty(Flux.defer(() -> {
                                log.info("Для вопроса ID {} не найдено комментариев", questionId);
                                return Flux.empty();
                            }));
                })
                .doOnComplete(() ->
                        log.info("Завершена загрузка комментариев для вопроса ID {}", questionId)
                )
                .doOnError(e ->
                        log.error("Ошибка при получении комментариев для вопроса ID {}: {}", questionId, e.getMessage())
                );
    }
}