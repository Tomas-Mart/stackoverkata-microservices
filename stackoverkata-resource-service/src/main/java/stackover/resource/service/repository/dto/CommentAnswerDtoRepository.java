package stackover.resource.service.repository.dto;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.CommentAnswerResponseDto;
import stackover.resource.service.entity.question.answer.CommentAnswer;

@Repository
public interface CommentAnswerDtoRepository extends ReactiveCrudRepository<CommentAnswer, Long> {

    /**
     * Находит комментарии по ID комментария и/или ID ответа
     *
     * @param commentId ID комментария (может быть null)
     * @param answerId  ID ответа (может быть null)
     * @return Flux с DTO комментариев, соответствующих критериям поиска
     */
    @Query("""
            SELECT 
                c.comment_text_id as id,
                c.answer_id as answerId,
                ct.last_redaction_date as lastUpdateDateTime,
                ct.persist_date as persistDateTime,
                ct.text,
                ct.user_id as userId,
                u.image_link as imageLink,
                (SELECT COALESCE(SUM(r.count), 0)
                 FROM reputation r
                 WHERE r.author_id = ct.user_id) as reputationCount
            FROM comment_answer c
            JOIN comment ct ON c.comment_text_id = ct.id
            LEFT JOIN user_entity u ON ct.user_id = u.id
            WHERE (:commentId IS NULL OR c.comment_text_id = :commentId)
            AND (:answerId IS NULL OR c.answer_id = :answerId)
            ORDER BY 
                CASE WHEN :answerId IS NOT NULL THEN ct.persist_date END DESC
            """)
    Flux<CommentAnswerResponseDto> findCommentAnswer(
            @Param("commentId") Long commentId,
            @Param("answerId") Long answerId
    );

    /**
     * Находит один комментарий по ID
     *
     * @param commentId ID комментария
     * @return Mono с DTO комментария
     */
    default Mono<CommentAnswerResponseDto> findCommentAnswerById(Long commentId) {
        return findCommentAnswer(commentId, null)
                .next()
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Комментарий не найден")));
    }
}