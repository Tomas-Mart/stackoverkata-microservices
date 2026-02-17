package stackover.resource.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import stackover.resource.service.entity.question.VoteTypeQuestion;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
        description = "DTO для возврата информации о вопросе",
        example = """
                {
                  "id": 1,
                  "title": "Как использовать Spring Boot?",
                  "authorId": 123,
                  "authorName": "Иван Иванов",
                  "email": "user@example.com",
                  "authorImage": "https://example.com/avatar.jpg",
                  "description": "Пытаюсь разобраться с Spring Boot...",
                  "viewCount": 150,
                  "authorReputation": 500,
                  "countAnswer": 5,
                  "countValuable": 10,
                  "persistDateTime": "2023-01-15T10:30:00",
                  "lastUpdateDateTime": "2023-01-20T15:45:00",
                  "countVote": 25,
                  "voteTypeQuestion": "UP",
                  "listTagDto": [
                    {
                      "id": 42,
                      "name": "java",
                      "description": "Вопросы по программированию на Java",
                      "persistDateTime": "2023-01-10T12:00:00"
                    },
                    {
                      "id": 43,
                      "name": "spring-boot",
                      "description": "Фреймворк для создания Spring-приложений",
                      "persistDateTime": "2023-01-11T14:30:00"
                    }
                  ]
                }"""
)
public record QuestionResponseDto(
        @Schema(description = "ID вопроса", example = "1")
        Long id,
        @Schema(description = "Заголовок вопроса", example = "Как работать с Spring Boot?")
        String title,
        @Schema(description = "ID автора", example = "123")
        Long authorId,
        @Schema(description = "Имя автора", example = "Иван Иванов")
        String authorName,
        @Schema(description = "Почта пользователя", example = "user@example.com")
        String email,
        @Schema(description = "Ссылка на изображение автора", example = "https://example.com/avatar.jpg")
        String authorImage,
        @Schema(description = "Описание вопроса", example = "Пытаюсь разобраться со Spring Boot...")
        String description,
        @Schema(description = "Количество просмотров", example = "150")
        Long viewCount,
        @Schema(description = "Репутация автора", example = "500")
        Long authorReputation,
        @Schema(description = "Количество ответов на вопрос", example = "5")
        Long countAnswer,
        @Schema(description = "Рейтинг вопроса", example = "10")
        Long countValuable,
        @Schema(description = "Дата создания вопроса", example = "2023-01-15T10:30:00")
        LocalDateTime persistDateTime,
        @Schema(description = "Дата последнего обновления", example = "2023-01-20T15:45:00")
        LocalDateTime lastUpdateDateTime,
        @Schema(description = "Количество голосов за вопрос", example = "25")
        Long countVote,
        @Schema(
                description = "Голос авторизованного пользователя за вопрос",
                allowableValues = {"UP", "DOWN", "null"},
                example = "UP"
        )
        VoteTypeQuestion voteTypeQuestion,
        @Schema(description = "Список тегов")
        List<TagResponseDto> listTagDto
) {
    public QuestionResponseDto(
            Long id,
            String title,
            Long authorId,
            String authorName,
            String authorImage,
            String description,
            Long viewCount,
            Long authorReputation,
            Long countAnswer,
            Long countValuable,
            LocalDateTime persistDateTime,
            LocalDateTime lastUpdateDateTime,
            Long countVote,
            VoteTypeQuestion voteTypeQuestion
    ) {
        this(
                id, title, authorId, authorName,
                null, authorImage, description, viewCount,
                authorReputation, countAnswer, countValuable,
                persistDateTime, lastUpdateDateTime,
                countVote, voteTypeQuestion, null
        );
    }
}