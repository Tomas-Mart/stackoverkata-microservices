package stackover.resource.service.service.entity;

import reactor.core.publisher.Mono;
import stackover.resource.service.dto.request.AnswerRequestDto;
import stackover.resource.service.entity.question.answer.Answer;

public interface AnswerService {

    Mono<Answer> getAnswerById(Long id);

    Mono<Answer> getAnswerByIdAndUserIdNot(Long id, Long userId);

    Mono<Answer> updateAnswerBody(Long questionId, Long answerId, AnswerRequestDto requestDto, Long accountId);

    Mono<Answer> findById(Long id);

    Mono<Boolean> existsByIdAndQuestionId(Long id, Long questionId);
}