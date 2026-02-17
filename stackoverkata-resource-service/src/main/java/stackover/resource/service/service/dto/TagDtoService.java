package stackover.resource.service.service.dto;

import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.TagResponseDto;

import java.util.List;

public interface TagDtoService {

    Mono<List<TagResponseDto>> findTagsByQuestionId(Long questionId);

    Mono<List<TagResponseDto>> getTop3TagsByUserId(Long userId);
}