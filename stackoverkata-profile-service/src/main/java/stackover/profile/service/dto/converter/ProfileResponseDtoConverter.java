package stackover.profile.service.dto.converter;

import stackover.profile.service.dto.ProfileResponseDto;
import stackover.profile.service.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileResponseDtoConverter {

    public ProfileResponseDto toDto(Profile profile) {
        return ProfileResponseDto.builder()
                .profileId(profile.getId())
                .accountId(profile.getAccountId())
                .email(profile.getEmail())
                .fullName(profile.getFullName())
                .city(profile.getCity())
                .persistDateTime(profile.getPersistDateTime())
                .linkSite(profile.getLinkSite())
                .linkGitHub(profile.getLinkGitHub())
                .linkVk(profile.getLinkVk())
                .about(profile.getAbout())
                .imageLink(profile.getImageLink())
                .nickname(profile.getNickname())
                .build();
    }
}
