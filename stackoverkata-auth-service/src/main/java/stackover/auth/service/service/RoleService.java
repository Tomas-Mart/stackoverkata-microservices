package stackover.auth.service.service;

import reactor.core.publisher.Mono;
import stackover.auth.service.model.Role;
import stackover.auth.service.util.enums.RoleNameEnum;

/**
 * Сервис для работы с ролями пользователей
 */
public interface RoleService {

    Mono<Role> findByName(RoleNameEnum name);

    Mono<Boolean> existsByName(RoleNameEnum role);

    Mono<Boolean> checkExistByIdAndRole(long id, RoleNameEnum role);

    Mono<RoleNameEnum> getRoleById(Long roleId);

    Mono<Role> save(Role role); // Изменено с RuleBuilder на Mono<Role>

    Mono<Long> count(); // Добавлен метод count для инициализации данных
}