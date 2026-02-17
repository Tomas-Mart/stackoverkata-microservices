package stackover.resource.service.service.dto;

import reactor.core.publisher.Mono;
import stackover.resource.service.dto.request.AnswerRequestDto;
import stackover.resource.service.dto.response.AnswerResponseDto;

import java.util.List;

public interface AnswerDtoService {

    Mono<AnswerResponseDto> updateAnswerBody(Long questionId, Long answerId, AnswerRequestDto requestDto, Long accountId);

    Mono<List<AnswerResponseDto>> getAllAnswersDtoByQuestionId(Long questionId, Long accountId);
}