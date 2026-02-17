package stackover.auth.service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.account.AccountResponseDto;
import stackover.auth.service.model.Account;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {

    @Query("""
            SELECT a.id, a.email, r.name as role, a.enabled
            FROM account a
            JOIN role r ON a.role_id = r.id
            WHERE a.id = $1
            """)
    Mono<AccountResponseDto> getAccountById(long id);

    Mono<Account> findByEmail(String email);
}