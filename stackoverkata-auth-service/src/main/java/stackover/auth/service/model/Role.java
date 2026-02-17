package stackover.auth.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;
import stackover.auth.service.util.enums.RoleNameEnum;

@Data
@Table("role")
@NoArgsConstructor
public class Role {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private RoleNameEnum name;

    public Role(Long id, RoleNameEnum name) {
        this.id = id;
        this.name = name;
    }

    public static Mono<Role> create(RoleNameEnum name) {
        return Mono.just(new Role(null, name));
    }
}