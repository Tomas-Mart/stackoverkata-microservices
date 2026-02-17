package stackover.resource.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "DTO для ответа с комментарием к вопросу")
public record CommentQuestionResponseDto(
        @Schema(description = "Идентификатор комментария")
        Long id,

        @Schema(description = "Идентификатор вопроса")
        Long questionId,

        @Schema(description = "Дата последней редакции комментария")
        LocalDateTime lastRedactionDate,

        @Schema(description = "Дата создания комментария")
        LocalDateTime persistDate,

        @NotNull
        @NotEmpty
        @Schema(description = "Текст комментария")
        String text,

        @NotNull
        @Schema(description = "Идентификатор пользователя, оставившего комментарий")
        Long userId,

        @Schema(description = "Ссылка на изображение")
        String imageLink,

        @Schema(description = "Репутация пользователя")
        Long reputation
) {} 