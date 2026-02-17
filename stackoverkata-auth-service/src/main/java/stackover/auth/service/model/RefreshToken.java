package stackover.auth.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@NoArgsConstructor
@Table("refresh_token")
public class RefreshToken {
    @Id
    @Column("id")
    private Long id;

    @Column("account_id")
    private Long accountId;

    @Column("token")
    private String token;

    @Column("expiry_date")
    private Instant expiryDate;
}