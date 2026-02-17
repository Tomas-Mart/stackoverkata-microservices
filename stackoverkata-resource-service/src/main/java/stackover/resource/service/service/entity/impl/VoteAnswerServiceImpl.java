package stackover.resource.service.service.entity.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.answer.Answer;
import stackover.resource.service.entity.question.answer.VoteAnswer;
import stackover.resource.service.entity.question.answer.VoteTypeAnswer;
import stackover.resource.service.entity.user.User;
import stackover.resource.service.exception.AnswerException;
import stackover.resource.service.repository.entity.VoteAnswerRepository;
import stackover.resource.service.service.entity.AnswerService;
import stackover.resource.service.service.entity.ReputationService;
import stackover.resource.service.service.entity.UserService;
import stackover.resource.service.service.entity.VoteAnswerService;

import java.time.LocalDateTime;

@Slf4j
@Service
public class VoteAnswerServiceImpl extends AbstractServiceImpl<VoteAnswer, Long, VoteAnswerRepository>
        implements VoteAnswerService {

    private final UserService userService;
    private final AnswerService answerService;
    private final ReputationService reputationService;

    public VoteAnswerServiceImpl(VoteAnswerRepository voteAnswerRepository,
                                 UserService userService,
                                 AnswerService answerService,
                                 ReputationService reputationService) {
        super(voteAnswerRepository);
        this.userService = userService;
        this.answerService = answerService;
        this.reputationService = reputationService;
    }

    @Override
    protected Long getId(VoteAnswer entity) {
        return entity.getId();
    }

    @Override
    public Mono<Long> upVoteAnswer(Long questionId, Long answerId, Long accountId) {
        log.info("Setting up vote for answer {} on question {} by user {}",
                answerId, questionId, accountId);
        return processVote(answerId, accountId, VoteTypeAnswer.UP);
    }

    @Override
    public Mono<Long> downVoteAnswer(Long questionId, Long answerId, Long accountId) {
        log.info("Setting down vote for answer {} on question {} by user {}",
                answerId, questionId, accountId);
        return processVote(answerId, accountId, VoteTypeAnswer.DOWN);
    }

    private Mono<Long> processVote(Long answerId, Long accountId, VoteTypeAnswer voteType) {
        return userService.getUserByAccountId(accountId)
                .switchIfEmpty(Mono.defer(() -> {
                    // Логирование случая, когда пользователь не найден
                    log.error("Пользователь с ID {} не найден", accountId);
                    return Mono.error(new RuntimeException("Пользователь не найден"));
                }))
                .flatMap(voter -> answerService.getAnswerById(answerId)
                        .switchIfEmpty(Mono.defer(() -> {
                            // Логирование случая, когда ответ не найден
                            log.error("Ответ с ID {} не найден", answerId);
                            return Mono.error(new AnswerException("Ответ не найден"));
                        }))
                        .flatMap(answer -> {
                            // Проверка, что пользователь не голосует за свой собственный ответ
                            if (answer.getUser().getAccountId().equals(accountId)) {
                                log.error("Пользователь {} попытался проголосовать за свой ответ {}",
                                        accountId, answerId);
                                return Mono.error(new AnswerException("Пользователь не может голосовать за свой ответ"));
                            }
                            // Основная логика обработки голоса
                            return processVoteOperation(answer, voter, voteType)
                                    .doOnSuccess(count -> log.info("Голос успешно учтён. Всего голосов: {}", count))
                                    .doOnError(e -> log.error("Ошибка при обработке голоса: {}", e.getMessage()));
                        })
                );
    }

    private Mono<Long> processVoteOperation(Answer answer, User voter, VoteTypeAnswer voteType) {
        Mono<Void> reputationAction = voteType == VoteTypeAnswer.UP
                ? reputationService.increaseReputationForAnswerUpVote(answer, voter)
                : reputationService.decreaseReputationForAnswerDownVote(answer, voter);

        return reputationAction
                .then(saveOrUpdateVote(answer, voter, voteType))
                .then(repository.countByAnswerId(answer.getId()))
                .doOnSuccess(count -> log.info("{} vote set. Total votes: {}",
                        voteType == VoteTypeAnswer.UP ? "Up" : "Down", count))
                .doOnError(e -> log.error("Error setting vote: {}", e.getMessage()));
    }

    private Mono<VoteAnswer> saveOrUpdateVote(Answer answer, User voter, VoteTypeAnswer voteType) {
        return repository.findByUserAndAnswer(voter, answer)
                .flatMap(existingVote -> {
                    existingVote.setVoteTypeAnswer(voteType);
                    existingVote.setPersistDateTime(LocalDateTime.now());
                    return repository.save(existingVote);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    VoteAnswer newVote = new VoteAnswer();
                    newVote.setAnswer(answer);
                    newVote.setUser(voter);
                    newVote.setVoteTypeAnswer(voteType);
                    newVote.setPersistDateTime(LocalDateTime.now());
                    return repository.save(newVote);
                }));
    }
}