package stackover.resource.service.entity.question.answer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import stackover.resource.service.entity.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "votes_on_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteAnswer {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Answer answer;

    @CreationTimestamp
    @Column(name = "persist_date", updatable = false)
    private LocalDateTime persistDateTime;

    @Enumerated(EnumType.STRING)
    private VoteTypeAnswer voteTypeAnswer;

    public VoteAnswer(User user, Answer answer, VoteTypeAnswer voteTypeAnswer) {
        this.user = user;
        this.answer = answer;
        this.voteTypeAnswer = voteTypeAnswer;
    }
}
