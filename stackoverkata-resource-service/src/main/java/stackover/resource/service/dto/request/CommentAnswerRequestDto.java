package stackover.resource.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "CommentAnswerRequestDto",
        description = "DTO для создания комментария к ответу"
)
public record CommentAnswerRequestDto(
        @NotBlank
        @Schema(description = "Текст комментария", example = "Этот ответ помог мне решить проблему",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String text
) {
}