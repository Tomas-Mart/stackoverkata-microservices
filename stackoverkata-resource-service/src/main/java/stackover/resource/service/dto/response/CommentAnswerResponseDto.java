package stackover.resource.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "DTO для ответа с комментарием к ответу на вопрос")
public record CommentAnswerResponseDto(
        @Schema(description = "id комментария", example = "1")
        Long id,

        @Schema(description = "id ответа", example = "5")
        Long answerId,

        @Schema(description = "дата редактирования")
        LocalDateTime lastRedactionDate,

        @Schema(description = "дата создания ответа")
        LocalDateTime persistDate,

        @NotNull @NotEmpty
        @Schema(description = "текст комментария", example = "Полезный ответ!")
        String text,

        @Schema(description = "id пользователя", example = "3")
        Long userId,

        @Schema(description = "почта пользователя", example = "user@example.com")
        String email,

        @Schema(description = "ссылка на картинку пользователя", example = "http://example.com/avatar.jpg")
        String imageLink,

        @Schema(description = "репутация", example = "100")
        Long reputation
) {
    public CommentAnswerResponseDto(
            Long id,
            Long answerId,
            LocalDateTime lastUpdateDateTime,
            LocalDateTime persistDateTime,
            String text,
            Long userId,
            String imageLink,
            Long reputation) {

        this(id, answerId, lastUpdateDateTime, persistDateTime, text, userId,
                null, imageLink, reputation);
    }
}