package stackover.resource.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import stackover.resource.service.entity.user.User;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class CommentText {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @NotNull
    @Column
    @JdbcTypeCode(Types.LONGVARCHAR)
    private String text;

    @Enumerated
    @NotNull
    @Column(columnDefinition = "int2")
    private CommentType commentType;

    @Column(name = "persist_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime persistDateTime;

    @Column(name = "last_redaction_date")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDateTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    public CommentText(CommentType commentType) {
        this.commentType = commentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentText commentText = (CommentText) o;
        return Objects.equals(id, commentText.id) &&
                Objects.equals(text, commentText.text) &&
                Objects.equals(persistDateTime, commentText.persistDateTime) &&
                Objects.equals(lastUpdateDateTime, commentText.lastUpdateDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, persistDateTime, lastUpdateDateTime);
    }
}
