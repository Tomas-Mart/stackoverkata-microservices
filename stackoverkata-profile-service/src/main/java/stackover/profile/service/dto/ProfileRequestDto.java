package stackover.profile.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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