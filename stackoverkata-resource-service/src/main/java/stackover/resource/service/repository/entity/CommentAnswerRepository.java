package stackover.resource.service.repository.entity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import stackover.resource.service.entity.question.answer.CommentAnswer;

@Repository
public interface CommentAnswerRepository extends ReactiveCrudRepository<CommentAnswer, Long> {
}