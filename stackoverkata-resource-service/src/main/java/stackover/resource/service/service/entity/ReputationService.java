package stackover.resource.service.service.entity;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.answer.Answer;
import stackover.resource.service.entity.user.User;

@Service
public interface ReputationService {

    Mono<Void> increaseReputationForAnswerUpVote(Answer answer, User voter);

    Mono<Void> decreaseReputationForAnswerDownVote(Answer answer, User voter);
}