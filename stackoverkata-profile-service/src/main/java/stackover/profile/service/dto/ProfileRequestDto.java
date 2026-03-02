package stackover.profile.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfileRequestDto(@NotNull Long accountId,
                                @NotBlank String email,
                                @NotBlank String fullName,
                                String city,
                                String linkSite,
                                String linkGitHub,
                                String linkVk,
                                String about,
                                String imageLink,
                                @NotBlank String nickname) {
}