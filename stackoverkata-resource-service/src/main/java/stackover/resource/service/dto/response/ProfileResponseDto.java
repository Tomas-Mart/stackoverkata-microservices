package stackover.resource.service.dto.response;

import stackover.resource.service.entity.user.RoleNameEnum;

import java.time.LocalDateTime;

public record ProfileResponseDto(
        Long profileId,
        Long accountId,
        String email,
        String fullName,
        String city,
        LocalDateTime persistDateTime,
        String linkSite,
        String linkGitHub,
        String linkVk,
        String about,
        String imageLink,
        String nickname,
        RoleNameEnum role,
        boolean enabled
) {
    // Конструктор для fallback с accountId
    public ProfileResponseDto(Long accountId) {
        this(null, accountId, null, null, null, null,
                null, null, null, null, null, null, null, false);
    }

    // Конструктор для создания fallback профиля
    public ProfileResponseDto(Long profileId, Long accountId) {
        this(profileId, accountId, null, null, null, null,
                null, null, null, null, null, null, null, false);
    }

    // Полный конструктор по умолчанию
    public ProfileResponseDto() {
        this(null, null, null, null, null, null,
                null, null, null, null, null, null, null, false);
    }
}