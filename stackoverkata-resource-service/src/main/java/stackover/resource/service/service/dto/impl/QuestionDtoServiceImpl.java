package stackover.resource.service.service.dto.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.request.QuestionCreateRequestDto;
import stackover.resource.service.dto.response.ProfileResponseDto;
import stackover.resource.service.dto.response.QuestionResponseDto;
import stackover.resource.service.dto.response.TagResponseDto;
import stackover.resource.service.exception.AccountNotAvailableException;
import stackover.resource.service.feign.AuthServiceClient;
import stackover.resource.service.feign.ProfileServiceClient;
import stackover.resource.service.repository.dto.QuestionDtoRepository;
import stackover.resource.service.service.dto.QuestionDtoService;
import stackover.resource.service.service.dto.TagDtoService;
import stackover.resource.service.service.entity.QuestionService;

import java.util.List;

/**
 * Реализация сервиса для работы с DTO вопросов
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionDtoServiceImpl implements QuestionDtoService {

    private final TagDtoService tagDtoService;
    private final QuestionService questionService;
    private final AuthServiceClient authServiceClient;
    private final ProfileServiceClient profileServiceClient;
    private final QuestionDtoRepository questionDtoRepository;

    @Override
    @Operation(
            summary = "Получение DTO вопроса",
            description = "Возвращает DTO вопроса с полной информацией, включая теги и статистику"
    )
    public Mono<QuestionResponseDto> getQuestionDtoByQuestionIdAndUserId(
            @Parameter(description = "ID вопроса", required = true) Long questionId,
            @Parameter(description = "ID пользователя", required = true) Long accountId) {

        log.info("Получение DTO вопроса {} для пользователя {}", questionId, accountId);

        return validateAccount(accountId)
                .then(retrieveQuestion(questionId, accountId))
                .flatMap(this::enrichQuestionWithProfileAndTags)
                .doOnSuccess(dto -> log.debug("Успешно получено DTO вопроса: {}", dto))
                .onErrorResume(e -> {
                    log.error("Ошибка при получении DTO вопроса {} для пользователя {}", questionId, accountId, e);
                    return Mono.error(e);
                });
    }

    @Override
    @Operation(summary = "Создание нового вопроса")
    @Transactional
    public Mono<QuestionResponseDto> addNewQuestion(
            @Parameter(description = "ID пользователя", required = true) Long accountId,
            @Parameter(description = "Данные для создания вопроса", required = true) QuestionCreateRequestDto requestDto) {

        log.info("Создание нового вопроса для пользователя {}", accountId);

        return validateAccount(accountId)
                .then(questionService.createNewQuestion(accountId, requestDto))
                .flatMap(question -> questionDtoRepository.getQuestionDtoByQuestionIdAndUserId(question.getId(), accountId))
                .flatMap(this::enrichQuestionWithProfileAndTags)
                .doOnSuccess(dto -> log.info("Успешно создан новый вопрос: {}", dto))
                .onErrorResume(e -> {
                    log.error("Ошибка при создании вопроса для пользователя {}", accountId, e);
                    return Mono.error(e);
                });
    }

    /**
     * Проверка доступности аккаунта
     */
    private Mono<Void> validateAccount(Long accountId) {
        log.debug("Проверка доступности аккаунта {}", accountId);
        return authServiceClient.isAccountExist(accountId)
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Аккаунт {} недоступен", accountId);
                    return Mono.error(new AccountNotAvailableException("Аккаунт недоступен"));
                }))
                .onErrorMap(e -> {
                    log.error("Ошибка при проверке аккаунта {}", accountId, e);
                    return new IllegalStateException("Сервис авторизации недоступен");
                })
                .then();
    }

    /**
     * Получение DTO вопроса из репозитория
     */
    private Mono<QuestionResponseDto> retrieveQuestion(Long questionId, Long accountId) {
        log.debug("Получение вопроса {} для пользователя {} из репозитория", questionId, accountId);
        return questionDtoRepository.getQuestionDtoByQuestionIdAndUserId(questionId, accountId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Вопрос {} не найден для пользователя {}", questionId, accountId);
                    return Mono.error(new IllegalArgumentException("Вопрос не найден"));
                }));
    }

    /**
     * Обогащение DTO вопроса дополнительными данными
     */
    private Mono<QuestionResponseDto> enrichQuestionWithProfileAndTags(QuestionResponseDto questionDto) {
        log.debug("Обогащение DTO вопроса {} данными профиля и тегов", questionDto.id());
        return getAuthorProfile(questionDto.authorId())
                .zipWith(getQuestionTags(questionDto.id()))
                .map(tuple -> buildEnrichedQuestion(questionDto, tuple.getT1(), tuple.getT2()))
                .doOnSuccess(dto -> log.debug("DTO вопроса успешно обогащено: {}", dto))
                .doOnError(e -> log.error("Ошибка при обогащении DTO вопроса {}", questionDto.id(), e));
    }

    /**
     * Получение профиля автора
     */
    private Mono<ProfileResponseDto> getAuthorProfile(Long authorId) {
        log.debug("Получение профиля автора {}", authorId);
        return profileServiceClient.getProfileByAccountId(authorId)
                .onErrorResume(e -> {
                    log.warn("Использование fallback профиля для автора {}", authorId);
                    return Mono.just(buildFallbackProfile(authorId));
                });
    }

    /**
     * Получение тегов вопроса
     */
    private Mono<List<TagResponseDto>> getQuestionTags(Long questionId) {
        log.debug("Получение тегов для вопроса {}", questionId);
        return tagDtoService.findTagsByQuestionId(questionId)
                .defaultIfEmpty(List.of())
                .doOnError(e -> log.error("Ошибка при получении тегов для вопроса {}", questionId, e));
    }

    /**
     * Создание обогащенного DTO вопроса
     */
    private QuestionResponseDto buildEnrichedQuestion(
            QuestionResponseDto question,
            ProfileResponseDto profile,
            List<TagResponseDto> tags) {

        return new QuestionResponseDto(
                question.id(),
                question.title(),
                question.authorId(),
                question.authorName(),
                profile.email(),
                question.authorImage(),
                question.description(),
                question.viewCount(),
                question.authorReputation(),
                question.countAnswer(),
                question.countValuable(),
                question.persistDateTime(),
                question.lastUpdateDateTime(),
                question.countVote(),
                question.voteTypeQuestion(),
                tags
        );
    }

    /**
     * Создание fallback профиля
     */
    private ProfileResponseDto buildFallbackProfile(Long accountId) {
        log.debug("Создание fallback профиля для accountId {}", accountId);
        return new ProfileResponseDto(
                null, accountId, null, null, null, null,
                null, null, null, null, null, null, null, false);
    }
}