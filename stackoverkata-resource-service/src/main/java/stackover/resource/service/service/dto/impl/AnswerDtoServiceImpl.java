package stackover.resource.service.service.dto.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.request.AnswerRequestDto;
import stackover.resource.service.dto.response.AnswerResponseDto;
import stackover.resource.service.dto.response.ProfileResponseDto;
import stackover.resource.service.entity.user.User;
import stackover.resource.service.exception.AnswerException;
import stackover.resource.service.feign.ProfileServiceClient;
import stackover.resource.service.repository.dto.AnswerDtoRepository;
import stackover.resource.service.service.dto.AnswerDtoService;
import stackover.resource.service.service.entity.AnswerService;
import stackover.resource.service.service.entity.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerDtoServiceImpl implements AnswerDtoService {

    private final UserService userService;
    private final AnswerService answerService;
    private final AnswerDtoRepository answerDtoRepository;
    private final ProfileServiceClient profileServiceClient;

    @Override
    public Mono<AnswerResponseDto> updateAnswerBody(Long questionId, Long answerId, AnswerRequestDto requestDto, Long accountId) {
        log.info("Начало обновления ответа ID {} пользователем {}", answerId, accountId);

        return answerService.updateAnswerBody(questionId, answerId, requestDto, accountId)
                .switchIfEmpty(Mono.error(new AnswerException("Ответ не найден", 404)))
                .flatMap(updatedAnswer -> fetchUserData(accountId))
                .flatMap(user -> fetchProfileData(accountId)
                        .flatMap(profile -> fetchAnswerDto(answerId, accountId)
                                .map(answerDto -> buildResponse(answerDto, profile))))
                .doOnSuccess(dto -> log.info("Успешно обновлен ответ ID {}", answerId))
                .doOnError(e -> log.error("Ошибка при обновлении ответа: {}", e.getMessage()));
    }

    @Override
    public Mono<List<AnswerResponseDto>> getAllAnswersDtoByQuestionId(Long questionId, Long accountId) {
        log.debug("Получение ответов для вопроса ID {}", questionId);

        return answerDtoRepository.getAnswersDtoByQuestionId(questionId, accountId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("Вопрос ID {} не найден или не имеет ответов", questionId);
                    return Mono.error(new AnswerException("Вопрос не найден", 404));
                }))
                .collectList()
                .doOnSuccess(list -> log.debug("Найдено {} ответов для вопроса ID {}", list.size(), questionId))
                .doOnError(e -> log.error("Ошибка получения ответов: {}", e.getMessage()));
    }

    private Mono<User> fetchUserData(Long accountId) {
        return userService.getUserByAccountId(accountId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Пользователь с ID {} не найден", accountId);
                    return Mono.error(new RuntimeException("Пользователь не найден"));
                }));
    }

    private Mono<ProfileResponseDto> fetchProfileData(Long accountId) {
        return profileServiceClient.getProfileByAccountId(accountId)
                .onErrorResume(e -> {
                    log.warn("Ошибка получения профиля, используется заглушка");
                    return Mono.just(createFallbackProfile(accountId));
                });
    }

    private Mono<AnswerResponseDto> fetchAnswerDto(Long answerId, Long accountId) {
        return answerDtoRepository.getAnswerDtoByAnswerIdAndUserId(answerId, accountId)
                .switchIfEmpty(Mono.error(new RuntimeException("Данные ответа не найдены")));
    }

    private AnswerResponseDto buildResponse(AnswerResponseDto answerDto, ProfileResponseDto profile) {
        return new AnswerResponseDto(
                answerDto.id(),
                answerDto.userId(),
                profile.email(),
                answerDto.questionId(),
                answerDto.body(),
                answerDto.persistDate(),
                answerDto.isHelpful(),
                answerDto.dateAccept(),
                answerDto.countValuable(),
                answerDto.countUserReputation(),
                answerDto.image(),
                answerDto.nickname(),
                answerDto.countVote(),
                answerDto.voteTypeAnswer()
        );
    }

    private ProfileResponseDto createFallbackProfile(Long accountId) {
        return new ProfileResponseDto(
                0L, accountId, null, null, null, null,
                null, null, null, null, null, null, null, false
        );
    }
}