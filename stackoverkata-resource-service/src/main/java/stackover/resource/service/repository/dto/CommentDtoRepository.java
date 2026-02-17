package stackover.resource.service.repository.dto;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import stackover.resource.service.dto.response.CommentQuestionResponseDto;
import stackover.resource.service.entity.question.CommentQuestion;

@Repository
public interface CommentDtoRepository extends ReactiveCrudRepository<CommentQuestion, Long> {

    @Query("""
                SELECT 
                    cq.id as id,
                    cq.question_id as questionId,
                    cq.last_update_date_time as lastRedactionDate,
                    cq.persist_date_time as persistDate,
                    cq.text as text,
                    u.id as userId,
                    u.image_link as imageLink,
                    (SELECT COALESCE(SUM(r.count), 0) 
                     FROM reputation r 
                     WHERE r.author_id = u.id) as reputation
                FROM comment_question cq
                JOIN users u ON cq.user_id = u.id
                WHERE cq.question_id = :questionId
                ORDER BY cq.persist_date_time DESC
            """)
    Flux<CommentQuestionResponseDto> getAllQuestionCommentDtoById(Long questionId);
}