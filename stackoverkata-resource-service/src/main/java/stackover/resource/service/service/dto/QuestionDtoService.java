package stackover.resource.service.service.dto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.request.QuestionCreateRequestDto;
import stackover.resource.service.dto.response.QuestionResponseDto;

/**
 * Сервис для работы с DTO вопросов
 */
public interface QuestionDtoService {

    /**
     * Получить DTO вопроса по ID вопроса и ID пользователя
     *
     * @param questionId ID вопроса
     * @param accountId  ID пользователя
     * @return Mono с QuestionResponseDto
     */
    @Operation(
            summary = "Получение DTO вопроса",
            description = "Возвращает DTO вопроса с полной информацией"
    )
    Mono<QuestionResponseDto> getQuestionDtoByQuestionIdAndUserId(
            @Parameter(description = "ID вопроса", required = true) Long questionId,
            @Parameter(description = "ID пользователя", required = true) Long accountId);

    /**
     * Создать новый вопрос
     *
     * @param accountId ID пользователя
     * @param request   DTO с данными для создания
     * @return Mono с созданным QuestionResponseDto
     */
    @Operation(summary = "Создание нового вопроса")
    Mono<QuestionResponseDto> addNewQuestion(
            @Parameter(description = "ID пользователя", required = true) Long accountId,
            @Parameter(description = "Данные для создания вопроса", required = true) QuestionCreateRequestDto request);
}