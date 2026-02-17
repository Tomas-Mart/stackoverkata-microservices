package stackover.resource.service.service.entity.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.request.AnswerRequestDto;
import stackover.resource.service.entity.question.answer.Answer;
import stackover.resource.service.repository.entity.AnswerRepository;
import stackover.resource.service.service.entity.AnswerService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

    @Override
    public Mono<Answer> getAnswerById(Long id) {
        return answerRepository.findById(id)
                .doOnSuccess(answer -> log.debug("Найден ответ с ID: {}", id))
                .doOnError(e -> log.error("Ошибка поиска ответа по ID {}: {}", id, e.getMessage()));
    }

    @Override
    public Mono<Answer> getAnswerByIdAndUserIdNot(Long id, Long userId) {
        return answerRepository.findByIdAndUserIdNot(id, userId)
                .doOnSuccess(answer -> log.debug("Найден ответ с ID {} для пользователя {}", id, userId))
                .switchIfEmpty(Mono.error(new RuntimeException("Ответ не найден")));
    }

    @Override
    public Mono<Answer> updateAnswerBody(Long questionId, Long answerId, AnswerRequestDto requestDto, Long accountId) {
        return existsByIdAndQuestionId(answerId, questionId)
                .flatMap(exists -> {
                    if (!exists) {
                        log.error("Ответ с ID {} для вопроса {} не найден", answerId, questionId);
                        return Mono.error(new RuntimeException("Ответ не найден"));
                    }
                    return findById(answerId)
                            .flatMap(answer -> {
                                answer.setHtmlBody(requestDto.body());
                                return answerRepository.save(answer)
                                        .doOnSuccess(a -> log.info("Обновлен ответ ID {} пользователем {}", answerId, accountId))
                                        .doOnError(e -> log.error("Ошибка сохранения ответа: {}", e.getMessage()));
                            });
                });
    }

    @Override
    public Mono<Answer> findById(Long id) {
        return answerRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Ответ с ID " + id + " не найден")));
    }

    @Override
    public Mono<Boolean> existsByIdAndQuestionId(Long id, Long questionId) {
        return answerRepository.existsByIdAndQuestionId(id, questionId);
    }
}