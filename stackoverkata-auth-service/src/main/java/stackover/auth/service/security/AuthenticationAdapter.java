package stackover.auth.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import stackover.auth.service.model.Account;
import stackover.auth.service.service.RoleService;
import stackover.auth.service.util.enums.RoleNameEnum;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class AuthenticationAdapter implements Authentication {

    private final Account account;
    private boolean authenticated = true;
    private final RoleService roleService;

    public AuthenticationAdapter(Account account, RoleService roleService) {
        this.account = account;
        this.roleService = roleService;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (account.getRoleId() != null) {
            Optional<RoleNameEnum> role = roleService.getRoleById(account.getRoleId()).blockOptional();
            if (role.isPresent()) {
                return Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + role.get().name())
                );
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return account.getPassword();
    }

    @Override
    public Object getDetails() {
        return account;
    }

    @Override
    public Object getPrincipal() {
        return account;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return account.getEmail();
    }
}