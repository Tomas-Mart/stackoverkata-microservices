package stackover.resource.service.entity.question;

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
@Table(name = "comment_question")
public class CommentQuestion {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    private CommentText commentText = new CommentText(CommentType.QUESTION);

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    public CommentQuestion(String text, User user) {
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
        if (this.commentText.getCommentType() != CommentType.QUESTION) {
            throw new ApiRequestException("У экземпляра CommentText, связанного с CommentQuestion, " +
                    "поле commentType должно принимать значение CommentType.QUESTION");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentQuestion that = (CommentQuestion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setText(String text) {
        commentText.setText(text);
    }

    public void setUser(User user) {
        commentText.setUser(user);
    }

    public void setCommentText(CommentText commentText) {
        this.commentText = commentText;
    }
}
