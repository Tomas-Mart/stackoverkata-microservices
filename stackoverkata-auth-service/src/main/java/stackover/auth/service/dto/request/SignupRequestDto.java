package stackover.auth.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;
import stackover.auth.service.dto.profile.ProfilePostDto;
import stackover.auth.service.model.Account;
import stackover.auth.service.util.enums.RoleNameEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * DTO для запроса регистрации нового пользователя
 */

@Builder
@Schema(description = "DTO для запроса регистрации")
public record SignupRequestDto(
        @Schema(description = "Пароль") @NotBlank String password,
        @Pattern(regexp = "USER|ADMIN", message = "Роль должна быть USER или ADMIN")
        @Schema(description = "Роль") @NotBlank String roleName,
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