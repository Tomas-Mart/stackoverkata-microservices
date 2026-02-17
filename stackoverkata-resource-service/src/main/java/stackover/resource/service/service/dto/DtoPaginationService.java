package stackover.resource.service.service.dto;

import stackover.resource.service.model.dto.PageDto;

import java.util.Map;

/**
 * Интерфейс для сервисов, отвечающих за сборку PageDto на основе универсальных параметров.
 *
 * Используется для реализации различных сценариев пагинации (например, по тегам, по пользователю и т.д.),
 * в том числе для одной и той же DTO.
 *
 * @param <T> тип возвращаемого DTO (например, QuestionDto, AnswerDto и т.д.)
 */

public interface DtoPaginationService<T> {
    /**
     * Возвращает объект PageDto, собранный на основе переданных параметров.
     *
     * Пример параметров:
     * - "workPagination" — ключ для определения сценария пагинации
     * - "currentPage", "itemsOnPage" — номера и размеры страниц
     * - любые дополнительные фильтры (например, "questionId", "userId", "tagList")
     *
     * @param params карта с параметрами запроса
     * @return объект PageDto с нужными данными
     */
    PageDto<T> getPageDto(Map<String, Object> params);
}
