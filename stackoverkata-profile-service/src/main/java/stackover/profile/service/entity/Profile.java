package stackover.profile.service.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Table("profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id
    @Column("id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column("account_id")
    @Setter(AccessLevel.NONE)
    @Min(value = 1, message = "incorrect accountId")
    private Long accountId;

    @Column("email")
    @NotBlank
    private String email;

    @Column("full_name")
    @NotBlank
    private String fullName;

    @Column("city")
    private String city;

    @Column("persist_date")
    @CreatedDate
    private LocalDateTime persistDateTime;

    @Column("link_site")
    private String linkSite;

    @Column("link_github")
    private String linkGitHub;

    @Column("link_vk")
    private String linkVk;

    @Column("about")
    private String about;

    @Column("image_link")
    private String imageLink;

    @Column("last_redaction_date")
    @LastModifiedDate
    private LocalDateTime lastUpdateDateTime;

    @Column("nickname")
    private String nickname;

    @Builder
    public Profile(Long accountId, String email, String fullName, String city, LocalDateTime persistDateTime,
                   String linkSite, String linkGitHub, String linkVk, String about, String imageLink, String nickname) {
        this.accountId = accountId;
        this.email = email;
        this.fullName = fullName;
        this.city = city;
        this.persistDateTime = persistDateTime;
        this.linkSite = linkSite;
        this.linkGitHub = linkGitHub;
        this.linkVk = linkVk;
        this.about = about;
        this.imageLink = imageLink;
        this.nickname = nickname;
    }

}
