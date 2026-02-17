package stackover.resource.service.repository.entity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.answer.Answer;
import stackover.resource.service.entity.question.answer.VoteAnswer;
import stackover.resource.service.entity.user.User;

@Repository
public interface VoteAnswerRepository extends ReactiveCrudRepository<VoteAnswer, Long> {

    Mono<VoteAnswer> findByUserAndAnswer(User user, Answer answer);

    Mono<Long> countByAnswerId(Long answerId);
}