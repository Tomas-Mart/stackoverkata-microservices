package stackover.resource.service.service.entity.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.user.User;
import stackover.resource.service.repository.entity.UserRepository;
import stackover.resource.service.service.entity.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> getUserByAccountId(Long accountId) {
        return userRepository.findByAccountId(accountId)
                .doOnSubscribe(sub -> log.debug("Начало поиска пользователя по accountId: {}", accountId))
                .doOnSuccess(user -> {
                    if (user != null) {
                        log.debug("Пользователь с accountId {} найден", accountId);
                    } else {
                        log.debug("Пользователь с accountId {} не найден", accountId);
                    }
                })
                .doOnError(e -> log.error("Ошибка при поиске пользователя по accountId {}: {}", accountId, e.getMessage()))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Пользователь с accountId {} не найден", accountId);
                    return Mono.empty();
                }));
    }
}