package stackover.auth.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import stackover.auth.service.exception.AccountExistException;
import stackover.auth.service.exception.EntityNotFoundException;
import stackover.auth.service.model.Role;
import stackover.auth.service.repository.RoleRepository;
import stackover.auth.service.service.RoleService;
import stackover.auth.service.util.enums.RoleNameEnum;

@Slf4j
@Service
@RequiredArgsConstructor
class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Mono<Role> findByName(RoleNameEnum name) {
        log.debug("Поиск роли по имени: {}", name);
        return roleRepository.findByName(name)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Role not found: " + name)))
                .doOnSuccess(r -> log.debug("Найдена роль: {}", r))
                .doOnError(e -> log.error("Ошибка поиска роли", e));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Boolean> existsByName(RoleNameEnum name) {
        log.debug("Проверка существования роли по имени: {}", name);
        return roleRepository.existsByName(name)
                .doOnSuccess(r -> log.debug("Роль существует: {}", r))
                .doOnError(e -> log.error("Ошибка проверки существования роли", e));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Boolean> checkExistByIdAndRole(long id, RoleNameEnum role) {
        log.debug("Проверка существования аккаунта ID {} с ролью {}", id, role);
        return roleRepository.checkExistByIdAndRole(id, role)
                .doOnSuccess(r -> log.debug("Результат проверки: {}", r))
                .doOnError(e -> log.error("Ошибка проверки существования аккаунта с ролью", e));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<RoleNameEnum> getRoleById(Long roleId) {
        if (roleId == null) {
            return Mono.just(RoleNameEnum.ROLE_USER);
        }
        return roleRepository.findById(roleId)
                .map(Role::getName)
                .defaultIfEmpty(RoleNameEnum.ROLE_USER)
                .onErrorResume(e -> Mono.error(new EntityNotFoundException("Failed to get role by id", e)));
    }

    @Override
    @Transactional
    public Mono<Role> save(Role role) {
        return roleRepository.save(role)
                .onErrorMap(e -> new AccountExistException("Failed to save role", e));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Long> count() {
        return roleRepository.count()
                .onErrorMap(e -> new AccountExistException("Failed to count roles", e));
    }
}