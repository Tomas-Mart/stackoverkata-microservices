package stackover.auth.service.dto.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import stackover.auth.service.util.enums.RoleNameEnum;

import java.time.LocalDateTime;

@Builder
@Schema(description = "DTO для ответа с данными профиля")
public record ProfileResponseDto(
        @Schema(description = "ID профиля") Long profileId,
        @Schema(description = "ID аккаунта") Long accountId,
        @Schema(description = "Email") String email,
        @Schema(description = "Полное имя") String fullName,
        @Schema(description = "Город") String city,
        @Schema(description = "Дата создания") LocalDateTime persistDateTime,
        @Schema(description = "Ссылка на сайт") String linkSite,
        @Schema(description = "Ссылка на GitHub") String linkGitHub,
        @Schema(description = "Ссылка на VK") String linkVk,
        @Schema(description = "О себе") String about,
        @Schema(description = "Ссылка на изображение") String imageLink,
        @Schema(description = "Никнейм") String nickname,
        @Schema(description = "Роль") RoleNameEnum role,
        @Schema(description = "Статус") Boolean enabled
) {

    public static ProfileResponseDto createFallbackResponse(Long accountId) {
        return new ProfileResponseDto(
                -1L, accountId, "service@unavailable.com", "Service Unavailable", null,
                LocalDateTime.now(), null, null, null, "Profile service unavailable",
                null, "unavailable", RoleNameEnum.ROLE_USER, false
        );
    }
}