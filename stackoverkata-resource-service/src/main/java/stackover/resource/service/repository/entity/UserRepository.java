package stackover.resource.service.repository.entity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.user.User;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Override
    Mono<User> findById(Long id);

    Mono<User> findFirstByOrderByIdAsc();

    Mono<User> findByAccountId(Long accountId);

}