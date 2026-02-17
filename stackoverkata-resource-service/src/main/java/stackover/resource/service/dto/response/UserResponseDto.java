package stackover.resource.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO для ответа с информацией о пользователе")
public record UserResponseDto(
        @Schema(description = "Уникальный идентификатор пользователя")
        Long id,

        @Schema(description = "Email пользователя")
        String email,

        @Schema(description = "Полное имя пользователя")
        String fullName,

        @Schema(description = "Ссылка на аватар пользователя")
        String linkImage,

        @Schema(description = "Город пользователя")
        String city,

        @Schema(description = "Репутация пользователя")
        Long reputation,

        @Schema(description = "Дата регистрации пользователя")
        LocalDateTime registrationDate,

        @Schema(description = "Количество голосов пользователя")
        Long votes

        // TODO: Добавить после реализации listTop3TagDto
        // @Schema(description = "Список топ-3 тегов пользователя")
        // List<TagResponseDto> listTop3TagDto
) {
        // конструктор без email
        public UserResponseDto(Long id, String fullName, String linkImage,
                               String city, Long reputation, LocalDateTime registrationDate, Long votes) {
                this(id, null, fullName, linkImage, city, reputation, registrationDate, votes);
        }
        public UserResponseDto() {
                this(null, null, null, null, null, null, null, null);
        }
}

