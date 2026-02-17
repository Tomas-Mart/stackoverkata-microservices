package stackover.auth.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.auth.service.service.AccountService;
import stackover.auth.service.service.RoleService;

@Service
@RequiredArgsConstructor
public class AccountReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final RoleService roleService;
    private final AccountService accountService;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return accountService.findByEmail(email)
                .flatMap(account -> roleService.getRoleById(account.getRoleId())
                        .map(role -> User.withUsername(account.getEmail())
                                .password(account.getPassword())
                                .authorities(role.name())
                                .build()));
    }
}