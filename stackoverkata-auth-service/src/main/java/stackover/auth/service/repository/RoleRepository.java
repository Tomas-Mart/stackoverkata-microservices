package stackover.auth.service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stackover.auth.service.model.Role;
import stackover.auth.service.util.enums.RoleNameEnum;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {

    Mono<Role> findByName(RoleNameEnum role);

    @Query("""
            SELECT EXISTS(
                SELECT 1 FROM account a 
                JOIN role r ON a.role_id = r.id 
                WHERE a.id = $1 
                AND r.name = $2
            )
            """)
    Mono<Boolean> checkExistByIdAndRole(Long id, RoleNameEnum role);

    @Query("SELECT EXISTS(SELECT 1 FROM role WHERE name = $1)")
    Mono<Boolean> existsByName(RoleNameEnum name);
}