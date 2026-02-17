package stackover.resource.service.service.entity;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.user.User;

@Service
public interface UserService {

    Mono<User> getUserByAccountId(Long accountId);
}
