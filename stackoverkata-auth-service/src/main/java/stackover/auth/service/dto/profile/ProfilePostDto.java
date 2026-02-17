package stackover.auth.service.dto.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Schema(description = "DTO для создания профиля")
public record ProfilePostDto(
        @Schema(description = "ID аккаунта") @NotNull Long accountId,
        @Schema(description = "Email") @NotBlank @Email String email,
        @Schema(description = "Полное имя") @NotBlank String fullName,
        @Schema(description = "Город") String city,
        @Schema(description = "Дата создания") LocalDateTime persistDateTime,
        @Schema(description = "Ссылка на сайт") String linkSite,
        @Schema(description = "Ссылка на GitHub") String linkGitHub,
        @Schema(description = "Ссылка на VK") String linkVk,
        @Schema(description = "О себе") String about,
        @Schema(description = "Ссылка на изображение") String imageLink,
        @Schema(description = "Никнейм") @NotBlank String nickname
) {
}
