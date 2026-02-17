package stackover.resource.service.service.dto;

import reactor.core.publisher.Flux;
import stackover.resource.service.dto.response.CommentQuestionResponseDto;

public interface CommentDtoService {

    Flux<CommentQuestionResponseDto> getAllCommentsOnQuestion(
            Long questionId,
            Long accountId);
}