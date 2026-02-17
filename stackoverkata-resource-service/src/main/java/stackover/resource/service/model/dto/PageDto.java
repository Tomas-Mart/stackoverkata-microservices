package stackover.resource.service.model.dto;

import lombok.Data;

import java.util.List;
    /**
    * Универсальный DTO для предоставления результата пагинации
    *
    *@param <T> тип элементов, отображаемых на странице (AnswerDto, QuestionDto, CommentDto, TagDto)
    */
@Data
public class PageDto<T> {
    private int currentPageNumber;          //номер текущей страницы
    private int totalPageCount;             //общее количество страниц
    private int totalResultCount;           //общее количество результатов в выборке без учёта пагинации
    private int itemsOnPage;                //количество элементов на странице
    private List<T> items;                  //список элементов
}
