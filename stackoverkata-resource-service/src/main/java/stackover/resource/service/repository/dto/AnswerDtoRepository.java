package stackover.resource.service.repository.dto;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.AnswerResponseDto;
import stackover.resource.service.entity.question.answer.Answer;

@Repository
public interface AnswerDtoRepository extends ReactiveCrudRepository<Answer, Long> {

    @Query("""
                SELECT 
                    a.id as id,
                    a.account_id as userId,
                    a.question_id as questionId,
                    a.html_body as body,
                    a.persist_date as persistDate,
                    a.is_helpful as isHelpful,
                    a.date_accept_time as dateAccept,
                    (SELECT COALESCE(SUM(CASE WHEN va.vote_type_answer = 'UP' THEN 1 ELSE -1 END), 0) 
                     FROM votes_on_answers va WHERE va.answer_id = a.id) as countValuable,
                    (SELECT COALESCE(SUM(r.count), 0) 
                     FROM reputation r WHERE r.author_id = a.account_id) as countUserReputation,
                    u.image_link as image,
                    u.nickname as nickname,
                    (SELECT COUNT(*) FROM votes_on_answers va WHERE va.answer_id = a.id) as countVote,
                    (SELECT va.vote_type_answer FROM votes_on_answers va 
                     WHERE va.answer_id = a.id AND va.user_id = :accountId) as voteTypeAnswer
                FROM answer a
                JOIN user_entity u ON a.account_id = u.id
                WHERE a.id = :answerId AND a.is_deleted = false
            """)
    Mono<AnswerResponseDto> getAnswerDtoByAnswerIdAndUserId(Long answerId, Long accountId);

    @Query("""
            SELECT 
                a.id as id,
                a.account_id as userId,
                a.question_id as questionId,
                a.html_body as body,
                a.persist_date as persistDate,
                a.is_helpful as isHelpful,
                a.date_accept_time as dateAccept,
                (SELECT COALESCE(SUM(CASE WHEN va.vote_type_answer = 'UP' THEN 1 ELSE -1 END), 0) 
                 FROM votes_on_answers va WHERE va.answer_id = a.id) as countValuable,
                (SELECT COALESCE(SUM(r.count), 0) 
                 FROM reputation r WHERE r.author_id = a.account_id) as countUserReputation,
                u.image_link as image,
                u.nickname as nickname,
                (SELECT COUNT(*) FROM votes_on_answers va WHERE va.answer_id = a.id) as countVote,
                (SELECT va.vote_type_answer FROM votes_on_answers va 
                 WHERE va.answer_id = a.id AND va.user_id = :accountId) as voteTypeAnswer
            FROM answer a
            JOIN user_entity u ON a.account_id = u.id
            JOIN question q ON a.question_id = q.id
            WHERE q.id = :questionId AND q.is_deleted = false AND a.is_deleted = false
            ORDER BY a.persist_date DESC
            """)
    Flux<AnswerResponseDto> getAnswersDtoByQuestionId(Long questionId, Long accountId);
}