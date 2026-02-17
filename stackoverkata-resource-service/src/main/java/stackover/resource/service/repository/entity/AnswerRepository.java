package stackover.resource.service.repository.entity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.answer.Answer;

@Repository
public interface AnswerRepository extends ReactiveCrudRepository<Answer, Long> {

    Mono<Answer> findById(Long id);

    Mono<Answer> findByIdAndUserIdNot(Long id, Long userId);

    Mono<Boolean> existsByIdAndQuestionId(Long id, Long questionId);
}