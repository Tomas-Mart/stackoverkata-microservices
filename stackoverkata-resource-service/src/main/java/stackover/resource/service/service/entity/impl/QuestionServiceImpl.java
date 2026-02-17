package stackover.resource.service.service.entity.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.request.QuestionCreateRequestDto;
import stackover.resource.service.dto.response.TagResponseDto;
import stackover.resource.service.entity.question.Question;
import stackover.resource.service.entity.question.Tag;
import stackover.resource.service.entity.user.User;
import stackover.resource.service.repository.entity.QuestionRepository;
import stackover.resource.service.service.entity.QuestionService;
import stackover.resource.service.service.entity.TagService;
import stackover.resource.service.service.entity.UserService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реактивная реализация сервиса для работы с сущностью Question
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {

    private final TagService tagService;
    private final UserService userService;
    private final QuestionRepository questionRepository;

    @Override
    public Mono<Question> findById(Long id) {
        log.debug("Поиск вопроса по ID: {}", id);
        return questionRepository.findById(id)
                .doOnSuccess(q -> log.debug("Найден вопрос: {}", q))
                .doOnError(e -> log.error("Ошибка поиска вопроса по ID {}", id, e));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Question> createNewQuestion(Long accountId, QuestionCreateRequestDto dto) {
        log.info("Создание нового вопроса для аккаунта {}", accountId);

        return userService.getUserByAccountId(accountId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Пользователь с accountId {} не найден", accountId);
                    return Mono.error(new IllegalArgumentException("Пользователь не найден"));
                }))
                .zipWith(tagService.findOrCreateTagsByNames(
                        dto.tags().stream()
                                .map(TagResponseDto::name)
                                .toList()
                ).collectList())
                .flatMap(tuple -> {
                    User user = tuple.getT1();
                    List<Tag> tags = tuple.getT2();
                    LocalDateTime now = LocalDateTime.now();

                    Question newQuestion = new Question();
                    newQuestion.setTitle(dto.title());
                    newQuestion.setDescription(dto.description());
                    newQuestion.setUser(user);
                    newQuestion.setTags(tags);
                    newQuestion.setIsDeleted(false);
                    newQuestion.setPersistDateTime(now);
                    newQuestion.setLastUpdateDateTime(now);

                    log.debug("Создана новая сущность вопроса: {}", newQuestion);
                    return questionRepository.save(newQuestion);
                })
                .doOnSuccess(question -> log.info("Вопрос успешно создан: {}", question))
                .doOnError(e -> log.error("Ошибка при создании вопроса", e));
    }
}