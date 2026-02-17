package stackover.resource.service.entity.question.answer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stackover.resource.service.entity.CommentText;
import stackover.resource.service.entity.CommentType;
import stackover.resource.service.entity.user.User;
import stackover.resource.service.exception.ApiRequestException;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment_answer")
public class CommentAnswer {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    private CommentText commentText = new CommentText(CommentType.ANSWER);

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    public CommentAnswer(String text, User user) {
        commentText.setText(text);
        commentText.setUser(user);
    }

    @PrePersist
    private void prePersistFunction() {
        checkConstraints();
    }

    @PreUpdate
    private void preUpdateFunction() {
        checkConstraints();
    }

    private void checkConstraints() {
        if (this.commentText.getCommentType() != CommentType.ANSWER) {
            throw new ApiRequestException("У экземпляра CommentText, связанного с CommentAnswer, " +
                    "поле commentType должно принимать значение CommentType.ANSWER");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentAnswer that = (CommentAnswer) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public void setText(String text) {
        commentText.setText(text);
    }

    public void setUser(User user) {
        commentText.setUser(user);
    }

    public void setComment(CommentText comment) {
        this.commentText = comment;
    }
}
