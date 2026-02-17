package stackover.resource.service.entity.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stackover.resource.service.entity.user.User;
import stackover.resource.service.exception.ConstrainException;

import java.time.LocalDateTime;

@Entity
@Table(name = "votes_on_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteQuestion {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Column(name = "persist_date", updatable = false)
    private LocalDateTime localDateTime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private VoteTypeQuestion voteTypeQuestion;

    public VoteQuestion(User user, Question question, VoteTypeQuestion voteTypeQuestion) {
        this.user = user;
        this.question = question;
        this.voteTypeQuestion = voteTypeQuestion;
    }

    @PrePersist
    private void prePersistFunction() {
        checkConstraints();
    }

    private void checkConstraints() {
        if (!voteTypeQuestion.equals(VoteTypeQuestion.UP) && !voteTypeQuestion.equals(VoteTypeQuestion.DOWN)) {
            throw new ConstrainException("В сущности VoteQuestion допускается передача значения в поле voteTypeQuestion только UP или DOWN");
        }
    }
}
