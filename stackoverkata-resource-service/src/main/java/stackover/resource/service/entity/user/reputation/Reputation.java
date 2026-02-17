package stackover.resource.service.entity.user.reputation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import stackover.resource.service.entity.question.Question;
import stackover.resource.service.entity.question.answer.Answer;
import stackover.resource.service.entity.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@CombinedNotNullQuestionOrAnswer
@Table(name = "reputation")
public class Reputation {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @CreationTimestamp
    @Column(name = "persist_date", updatable = false)
    private LocalDateTime persistDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @Column(name = "count")
    private Integer count;

    @Enumerated
    @NotNull
    @Column(name = "type")
    private ReputationType type;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Question.class, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Answer.class, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reputation reputation = (Reputation) o;
        return Objects.equals(id, reputation.id) &&
                Objects.equals(persistDate, reputation.persistDate) &&
                Objects.equals(count, reputation.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, persistDate, count);
    }
}
