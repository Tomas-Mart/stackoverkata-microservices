package stackover.resource.service.entity.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @Column
    @JdbcTypeCode(Types.LONGVARCHAR)
    private String message;

    @Column(name = "last_redaction_date", nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastRedactionDate;

    @Column(name = "persist_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime persistDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User userSender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;


    public Message(String message, User userSender, Chat chat) {
        this.message = message;
        this.userSender = userSender;
        this.chat = chat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(id, message1.id) &&
                Objects.equals(message, message1.message) &&
                Objects.equals(lastRedactionDate, message1.lastRedactionDate) &&
                Objects.equals(persistDate, message1.persistDate) &&
                Objects.equals(userSender, message1.userSender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, lastRedactionDate, persistDate, userSender);
    }
}
