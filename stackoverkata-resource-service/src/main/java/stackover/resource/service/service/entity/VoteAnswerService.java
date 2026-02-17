package stackover.resource.service.service.entity;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface VoteAnswerService {

    Mono<Long> upVoteAnswer(Long questionId, Long answerId, Long accountId);

    Mono<Long> downVoteAnswer(Long questionId, Long answerId, Long accountId);
}