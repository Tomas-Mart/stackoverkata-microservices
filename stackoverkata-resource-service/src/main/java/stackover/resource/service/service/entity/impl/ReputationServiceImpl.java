package stackover.resource.service.service.entity.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.answer.Answer;
import stackover.resource.service.entity.user.User;
import stackover.resource.service.entity.user.reputation.Reputation;
import stackover.resource.service.entity.user.reputation.ReputationType;
import stackover.resource.service.repository.entity.ReputationRepository;
import stackover.resource.service.service.entity.ReputationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReputationServiceImpl implements ReputationService {

    private final ReputationRepository reputationRepository;

    @Override
    public Mono<Void> increaseReputationForAnswerUpVote(Answer answer, User voter) {
        return reputationRepository.findByAnswerAndSender(answer, voter)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Создаем новую запись репутации для ответа {}", answer.getId());
                    return Mono.just(new Reputation());
                }))
                .flatMap(reputation -> {
                    reputation.setSender(voter);
                    reputation.setAuthor(answer.getUser());
                    reputation.setCount(10);
                    reputation.setAnswer(answer);
                    reputation.setType(ReputationType.ANSWER);

                    log.info("Повышаем репутацию автора ответа {} на +10", answer.getId());
                    return reputationRepository.save(reputation);
                })
                .then()
                .doOnSuccess(v -> log.info("Репутация автора ответа {} успешно повышена", answer.getId()))
                .doOnError(e -> log.error("Ошибка при повышении репутации: {}", e.getMessage()));
    }

    @Override
    public Mono<Void> decreaseReputationForAnswerDownVote(Answer answer, User voter) {
        return reputationRepository.findByAnswerAndSender(answer, voter)
                .flatMap(reputation -> {
                    reputation.setCount(-5);
                    log.info("Обновляем существующую запись репутации для ответа {}", answer.getId());
                    return reputationRepository.save(reputation);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    Reputation reputation = new Reputation();
                    reputation.setSender(voter);
                    reputation.setAuthor(answer.getUser());
                    reputation.setCount(-5);
                    reputation.setAnswer(answer);
                    reputation.setType(ReputationType.ANSWER);

                    log.info("Создаем новую запись репутации для ответа {}", answer.getId());
                    return reputationRepository.save(reputation);
                }))
                .then()
                .doOnSuccess(v -> log.info("Репутация автора ответа {} успешно понижена", answer.getId()))
                .doOnError(e -> log.error("Ошибка при понижении репутации: {}", e.getMessage()));
    }
}