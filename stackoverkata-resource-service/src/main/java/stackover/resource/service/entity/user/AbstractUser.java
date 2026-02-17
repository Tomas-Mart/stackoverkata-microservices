package stackover.resource.service.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "user_entity")
public class AbstractUser {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    protected Long id;

    @Column(name = "account_id")
    protected Long accountId;

    @Column
    protected String fullName;

    @Column(name = "persist_date", updatable = false)
    @CreationTimestamp
    protected LocalDateTime persistDateTime;

    @Column
    protected String city;

    @Column(name = "link_site")
    protected String linkSite;

    @Column(name = "link_github")
    protected String linkGitHub;

    @Column(name = "link_vk")
    protected String linkVk;

    @Column
    protected String about;

    @Column(name = "image_link")
    protected String imageLink;

    @Column(name = "last_redaction_date", nullable = false)
    @UpdateTimestamp
    protected LocalDateTime lastUpdateDateTime;

    @Column
    protected String nickname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUser abstractUser = (AbstractUser) o;
        return Objects.equals(id, abstractUser.id) &&
                Objects.equals(fullName, abstractUser.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName);
    }
}