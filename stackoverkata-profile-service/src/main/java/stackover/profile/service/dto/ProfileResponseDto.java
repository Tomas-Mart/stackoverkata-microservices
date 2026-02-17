package stackover.profile.service.dto;

import stackover.profile.service.enums.RoleNameEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ProfileResponseDto {

    long profileId;
    long accountId;
    String email;
    String fullName;
    String city;
    LocalDateTime persistDateTime;
    String linkSite;
    String linkGitHub;
    String linkVk;
    String about;
    String imageLink;
    String nickname;
    RoleNameEnum role;
    boolean enabled;

}
