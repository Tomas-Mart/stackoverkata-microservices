package stackover.profile.service.service;

import reactor.core.publisher.Mono;
import stackover.profile.service.dto.ProfilePostDto;
import stackover.profile.service.dto.ProfileRequestDto;
import stackover.profile.service.dto.ProfileResponseDto;
import stackover.profile.service.entity.Profile;

public interface ProfileService {

    Mono<Void> saveProfileByPostDto(ProfilePostDto profilePostDto);

    Mono<Profile> findProfileByAccountId(Long accountId);

    Mono<ProfileResponseDto> getProfileResponseDtoById(Long accountId);

    Mono<Boolean> existByAccountId(Long accountId);

    Mono<ProfileResponseDto> updateProfileByIdAndProfileRequestDto(Long accountId, ProfileRequestDto requestDto);
}