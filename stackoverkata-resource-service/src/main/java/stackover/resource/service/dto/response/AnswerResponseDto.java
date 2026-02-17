package stackover.resource.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import stackover.resource.service.entity.question.answer.VoteTypeAnswer;

import java.time.LocalDateTime;

@Schema(description = "DTO для ответа на вопрос")
public record AnswerResponseDto(
        @Schema(description = "ID ответа на вопрос", example = "1")
        Long id,
        @Schema(description = "ID пользователя", example = "1")
        Long userId,
        @Schema(description = "Почта пользователя", example = "user@example.com")
        String email,
        @Schema(description = "ID вопроса", example = "1")
        Long questionId,
        @Schema(description = "Текст ответа")
        String body,
        @Schema(description = "Дата создания ответа", example = "2025-06-04T12:00:00")
        LocalDateTime persistDate,
        @Schema(description = "Польза ответа", example = "true")
        Boolean isHelpful,
        @Schema(description = "Дата решения вопроса", example = "2025-06-04T12:30:00")
        LocalDateTime dateAccept,
        @Schema(description = "Рейтинг ответа", example = "10")
        Long countValuable,
        @Schema(description = "Рейтинг пользователя", example = "100")
        Long countUserReputation,
        @Schema(description = "Ссылка на картинку пользователя", example = "https://example.com/image.jpg")
        String image,
        @Schema(description = "Никнейм пользователя", example = "user_nickname")
        String nickname,
        @Schema(description = "Количество голосов", example = "5")
        Long countVote,
        @Schema(description = "Тип голоса", example = "UP_VOTE")
        VoteTypeAnswer voteTypeAnswer
) {
        public AnswerResponseDto(
                Long id, Long userId, Long questionId, String body,
                LocalDateTime persistDate, Boolean isHelpful, LocalDateTime dateAccept,
                Long countValuable, Long countUserReputation, String image, String nickname,
                Long countVote, VoteTypeAnswer voteTypeAnswer
        ) {
                this(id, userId, null, questionId, body, persistDate, isHelpful, dateAccept,
                        countValuable, countUserReputation, image, nickname, countVote, voteTypeAnswer);
        }
}