package stackover.profile.service.repository;

import stackover.profile.service.entity.Profile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProfileRepository extends ReactiveCrudRepository<Profile, Long> {

    Mono<Profile> findByAccountId(Long accountId);

    Mono<Boolean> existsByAccountId(Long accountId);
}