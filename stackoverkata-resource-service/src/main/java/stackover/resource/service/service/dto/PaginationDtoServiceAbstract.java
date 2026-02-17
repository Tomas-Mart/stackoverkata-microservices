package stackover.resource.service.service.dto;

import stackover.resource.service.model.dto.PageDto;

import java.util.List;
import java.util.Map;

/**
 * Абстрактный класс, предоставляющий базовую реализацию механизма пагинации.
 *
 * Позволяет разработчику реализовать только два метода:
 * - getItems — для получения элементов текущей страницы
 * - getTotalResultCount — для получения общего количества элементов
 *
 * @param <T> тип DTO (например, QuestionDto)
 * @param <P> тип параметра, по которому выполняется выборка (например, Long или Map<String, Object>)
 */

public abstract class PaginationDtoServiceAbstract<T, P> {

    /**
     * Получает список элементов для текущей страницы.
     */
    protected abstract List<T> getItems(P param);

    /**
     * Получает общее количество элементов по заданному параметру.
     */
    protected abstract int getTotalResultCount(P param);

    protected abstract P getParam(Map<String, Object> params);

    public PageDto<T> getPage(Map<String, Object> params) {

        /**
         * Формирует объект PageDto на основе переданных параметров.
         *
         * Параметры:
         * - currentPage - номер текущей страницы
         * - itemsOnPage - количество элементов на странице
         * - totalPageCount - количество страниц
         * - totalResultCount - общее количество элементов
         * @param params - мапа с параметрами
         */

        P param = getParam(params);
        List<T> items = getItems(param);

        int totalResultCount = getTotalResultCount(param);
        int currentPageNumber = (int) params.get("currentPage");
        int itemsOnPage = (int) params.get("itemsOnPage");
        int totalPageCount = (int) Math.ceil((double) totalResultCount / itemsOnPage);

        PageDto<T> pageDto = new PageDto<>();
        pageDto.setCurrentPageNumber(currentPageNumber);
        pageDto.setItemsOnPage(itemsOnPage);
        pageDto.setTotalPageCount(totalPageCount);
        pageDto.setTotalResultCount(totalResultCount);
        pageDto.setItems(items);

        return pageDto;
    }
}
