package stackover.profile.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ProfilePostDto(@NotNull Long accountId,
                             @NotBlank String email,
                             @NotBlank String fullName,
                             String city,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
                             LocalDateTime persistDateTime,
                             String linkSite,
                             String linkGitHub,
                             String linkVk,
                             String about,
                             String imageLink,
                             @NotBlank String nickname) {
}
