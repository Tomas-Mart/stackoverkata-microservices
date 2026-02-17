package stackover.resource.service.entity.chat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stackover.resource.service.entity.user.User;
import stackover.resource.service.exception.ApiRequestException;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "single_chat")
public class SingleChat {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    private Chat chat = new Chat(ChatType.SINGLE);

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User userOne;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User useTwo;

    @PrePersist
    private void prePersistFunction() {
        checkConstraints();
    }

    @PreUpdate
    private void preUpdateFunction() {
        checkConstraints();
    }

    private void checkConstraints() {
        if (this.chat.getChatType() != ChatType.SINGLE) {
            throw new ApiRequestException("У экземпляра Chat, связанного с SingleChat, " +
                    "поле chatType должно принимать значение ChatType.SINGLE");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleChat that = (SingleChat) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(chat, that.chat) &&
                Objects.equals(userOne, that.userOne) &&
                Objects.equals(useTwo, that.useTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chat, userOne, useTwo);
    }
}
