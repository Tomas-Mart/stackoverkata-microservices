package stackover.resource.service.repository.entity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.Question;

@Repository
public interface QuestionRepository extends ReactiveCrudRepository<Question, Long> {

    Mono<Question> findById(Long id);
}