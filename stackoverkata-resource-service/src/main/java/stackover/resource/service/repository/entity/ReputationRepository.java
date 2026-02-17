package stackover.resource.service.repository.entity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.Question;
import stackover.resource.service.entity.question.answer.Answer;
import stackover.resource.service.entity.user.User;
import stackover.resource.service.entity.user.reputation.Reputation;

@Repository
public interface ReputationRepository extends ReactiveCrudRepository<Reputation, Long> {

    @Override
    Mono<Reputation> findById(Long id);

    Mono<Reputation> findByQuestion(Question question);

    Mono<Reputation> findByAnswer(Answer answer);

    Mono<Reputation> findByAnswerAndSender(Answer answer, User sender);
}
