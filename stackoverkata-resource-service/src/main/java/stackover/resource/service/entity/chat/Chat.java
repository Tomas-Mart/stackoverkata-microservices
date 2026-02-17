package stackover.resource.service.entity.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR_POOLED")
    private Long id;

    @Column
    @JdbcTypeCode(Types.LONGVARCHAR)
    private String title;

    @Column(name = "persist_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime persistDate;

    @Enumerated
    @Column(columnDefinition = "int2")
    private ChatType chatType;

    public Chat(ChatType chatType) {
        this.chatType = chatType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) && Objects.equals(title, chat.title) && Objects.equals(persistDate, chat.persistDate) && chatType == chat.chatType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, persistDate, chatType);
    }
}