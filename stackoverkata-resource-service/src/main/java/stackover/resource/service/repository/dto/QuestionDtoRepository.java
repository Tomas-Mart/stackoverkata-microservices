package stackover.resource.service.repository.dto;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.QuestionResponseDto;
import stackover.resource.service.entity.question.Question;

@Repository
public interface QuestionDtoRepository extends ReactiveCrudRepository<Question, Long> {

    @Query("""
            SELECT
                q.id as id,
                q.title as title,
                q.account_id as authorId,
                u.full_name as authorName,
                u.image_link as authorImage,
                q.description as description,
                (SELECT COUNT(v.id) FROM question_viewed v WHERE v.question_id = q.id) as viewCount,
                (SELECT COALESCE(SUM(r.count), 0) FROM reputation r WHERE r.author_id = q.account_id) as authorReputation,
                (SELECT COUNT(a.id) FROM answer a WHERE a.question_id = q.id AND a.is_deleted = false) as countAnswer,
                (SELECT COUNT(vq.id) FROM votes_on_questions vq WHERE vq.question_id = q.id AND vq.vote_type_question = 'UP') as countValuable,
                q.persist_date as persistDateTime,
                q.last_redaction_date as lastUpdateDateTime,
                (SELECT COALESCE(SUM(CASE WHEN vq.vote_type_question = 'UP' THEN 1 WHEN vq.vote_type_question = 'DOWN' THEN -1 ELSE 0 END), 0)
                 FROM votes_on_questions vq WHERE vq.question_id = q.id) as countVote,
                (SELECT vq.vote_type_question FROM votes_on_questions vq
                 WHERE vq.question_id = q.id AND vq.user_id = :accountId) as voteTypeQuestion
            FROM question q
            JOIN user_entity u ON q.account_id = u.id
            WHERE q.id = :questionId AND q.is_deleted = false
            """)
    Mono<QuestionResponseDto> getQuestionDtoByQuestionIdAndUserId(
            @Param("questionId") Long questionId,
            @Param("accountId") Long accountId);


}