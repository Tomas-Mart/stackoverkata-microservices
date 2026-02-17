package stackover.resource.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO для обновления тела ответа")
public record AnswerRequestDto(
        @Schema(description = "Текст ответа", example = "Обновленный текст ответа")
        @NotEmpty @NotBlank @NotNull
        String body
) {}