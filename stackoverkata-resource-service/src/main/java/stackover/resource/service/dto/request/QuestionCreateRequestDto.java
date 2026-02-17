package stackover.resource.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import stackover.resource.service.dto.response.TagResponseDto;

import java.util.List;

@Schema(description = "DTO для создания вопроса")
public record QuestionCreateRequestDto(
        @NotBlank(message = "Заголовок вопроса не может быть пустым")
        @Size(max = 255, message = "Заголовок вопроса не может быть длиннее 255 символов")
        String title,

        @NotBlank(message = "Описание вопроса не может быть пустым")
        String description,

        @NotNull(message = "Список тегов не может быть null")
        @Size(min = 1, message = "Должен быть указан хотя бы один тег")
        List<TagResponseDto> tags
) {
}