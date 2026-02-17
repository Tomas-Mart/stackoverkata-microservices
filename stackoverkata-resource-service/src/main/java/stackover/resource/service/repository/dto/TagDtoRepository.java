package stackover.resource.service.repository.dto;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.TagResponseDto;
import stackover.resource.service.entity.question.Tag;

import java.util.List;

public interface TagDtoRepository extends ReactiveCrudRepository<Tag, Long> {

    @Query("""
            SELECT new stackover.resource.service.dto.response.TagResponseDto(
                t.id, t.name, t.description, t.persist_date
            )
            FROM Tag t JOIN t.questions q WHERE q.id = :questionId
            """)
    Mono<List<TagResponseDto>> findTagsByQuestionId(@Param("questionId") Long questionId);

    @Query("""
                SELECT new stackover.resource.service.dto.response.TagResponseDto(
                    t.id, t.name, t.description, t.persist_date,
                )
                FROM Tag t
                JOIN t.questions q
                JOIN q.answers a
                JOIN a.voteAnswers v
                WHERE v.user.id = :userId
                GROUP BY t.id, t.name, t.description, t.persist_date
                ORDER BY SUM(CASE WHEN v.voteTypeAnswer = 'UP' THEN 10 WHEN v.voteTypeAnswer = 'DOWN' THEN -5 ELSE 0 END) DESC
                LIMIT 3
            """)
    Mono<List<TagResponseDto>> getTop3TagsByUserId(@Param("userId") Long userId);
}