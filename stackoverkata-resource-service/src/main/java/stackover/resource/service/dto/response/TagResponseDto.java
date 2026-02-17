package stackover.resource.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(
        description = "DTO для информации о теге",
        example = """
                {
                  "id": 42,
                  "name": "java",
                  "description": "Вопросы по программированию на Java",
                  "persistDateTime": "2023-01-10T12:00:00"
                }"""
)
public record TagResponseDto(
        @Schema(description = "ID тега", example = "42")
        Long id,
        @Schema(description = "Название тега", example = "java")
        String name,
        @Schema(description = "Описание тега", example = "Вопросы по программированию на Java")
        String description,
        @Schema(description = "Дата создания тега", example = "2023-01-10T12:00:00")
        LocalDateTime persistDateTime
) {
}