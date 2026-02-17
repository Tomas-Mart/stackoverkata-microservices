package stackover.auth.service.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stackover.auth.service.model.RefreshToken;

import java.time.Instant;

@Repository
public interface RefreshTokenRepository extends ReactiveCrudRepository<RefreshToken, Long> {

    @Query("SELECT * FROM refresh_token WHERE token = $1")
    Mono<RefreshToken> findByToken(String token);

    @Query("SELECT * FROM refresh_token WHERE account_id = $1")
    Mono<RefreshToken> findByAccountId(Long accountId);

    @Modifying
    @Query("DELETE FROM refresh_token WHERE expiry_date <= $1")
    Mono<Integer> deleteAllExpiredSince(Instant now);

    @Modifying
    @Query("DELETE FROM refresh_token WHERE token = $1")
    Mono<Void> deleteByToken(String token);

    @Modifying
    @Query("DELETE FROM refresh_token WHERE account_id = $1")
    Mono<Void> deleteByAccountId(Long accountId);
}