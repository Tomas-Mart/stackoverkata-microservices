package stackover.resource.service.service.entity;

import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.answer.CommentAnswer;

public interface CommentAnswerService {
    /**
     * Добавляет комментарий к ответу
     *
     * @param answerId  ID ответа
     * @param accountId ID аккаунта пользователя
     * @param text      текст комментария
     * @return Mono с созданным комментарием или пустой Mono в случае ошибки
     */
    Mono<CommentAnswer> addCommentToAnswer(
            Long answerId,
            Long accountId,
            String text);
}