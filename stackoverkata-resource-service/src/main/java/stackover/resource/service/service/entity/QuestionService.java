package stackover.resource.service.service.entity;

import reactor.core.publisher.Mono;
import stackover.resource.service.dto.request.QuestionCreateRequestDto;
import stackover.resource.service.entity.question.Question;

/**
 * Сервис для работы с сущностью Question
 */
public interface QuestionService {

    /**
     * Найти вопрос по идентификатору
     *
     * @param id идентификатор вопроса
     * @return Mono с сущностью Question
     */
    Mono<Question> findById(Long id);

    /**
     * Создать новый вопрос
     *
     * @param accountId идентификатор аккаунта
     * @param dto       DTO с данными для создания вопроса
     * @return Mono с созданной сущностью Question
     */
    Mono<Question> createNewQuestion(Long accountId, QuestionCreateRequestDto dto);
}