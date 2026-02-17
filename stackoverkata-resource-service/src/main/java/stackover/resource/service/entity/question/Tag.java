package stackover.resource.service.entity.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import stackover.resource.service.exception.ConstrainException;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @NotNull
    @Column
    private String name;

    @Column
    @JdbcTypeCode(Types.LONGVARCHAR)
    private String description;

    @CreationTimestamp
    @Column(name = "persist_date", updatable = false)
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime persistDateTime;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<Question> questions;

    @PrePersist
    private void prePersistFunction() {
        checkConstraints();
    }

    @PreUpdate
    private void preUpdateFunction() {
        checkConstraints();
    }

    private void checkConstraints() {
        if (this.description.isEmpty()) {
            throw new ConstrainException("Поле description не должно быть пустым");
        }
        if (this.name.isEmpty()) {
            throw new ConstrainException("Поле name не должно быть пустым");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) &&
                Objects.equals(name, tag.name) &&
                Objects.equals(description, tag.description) &&
                Objects.equals(persistDateTime, tag.persistDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, persistDateTime);
    }


}
