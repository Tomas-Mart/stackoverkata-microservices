package stackover.auth.service.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Table("account")
@NoArgsConstructor
public class Account {
    @Id
    @Column("id")
    private Long id;

    @Email
    @Column("email")
    private String email;

    @NotNull
    @Column("password")
    private String password;

    @Column("enabled")
    private Boolean enabled = true;

    @Column("locale_tag")
    private String localeTag = "ru";

    @Column("role_id")
    private Long roleId;

    @Builder
    public Account(String email, String password, String localeTag, Long roleId) {
        this.email = email;
        this.password = password;
        this.localeTag = localeTag;
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(email, account.email) &&
                Objects.equals(password, account.password) &&
                Objects.equals(enabled, account.enabled) &&
                Objects.equals(localeTag, account.localeTag) &&
                Objects.equals(roleId, account.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, enabled, localeTag, roleId);
    }

    public static Mono<Account> create(String email, String password, String localeTag, Long roleId) {
        return Mono.just(Account.builder()
                .email(email)
                .password(password)
                .localeTag(localeTag)
                .roleId(roleId)
                .build());
    }
}