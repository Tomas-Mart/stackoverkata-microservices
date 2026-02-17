package stackover.resource.service.repository.dto;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.UserResponseDto;
import stackover.resource.service.entity.user.User;

public interface UserDtoRepository extends ReactiveCrudRepository<User, Long> {

    @Query("""
            SELECT u.id, u.full_name as fullName, u.image_link as imageLink, u.city,
                   COALESCE((SELECT SUM(r.count) FROM reputation r WHERE r.author_id = :userId), 0) as reputation,
                   u.persist_date as persistDateTime,
                   COALESCE((SELECT COUNT(*) FROM votes_on_questions vq WHERE vq.user_id = :userId), 0) +
                   COALESCE((SELECT COUNT(*) FROM votes_on_answers va WHERE va.user_id = :userId), 0) as votes
            FROM user_entity u WHERE u.id = :userId
            """)
    Mono<UserResponseDto> getUserDataBaseDto(Long userId);
}