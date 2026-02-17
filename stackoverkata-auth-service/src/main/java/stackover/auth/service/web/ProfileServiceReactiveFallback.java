package stackover.auth.service.web;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.profile.ProfilePostDto;
import stackover.auth.service.dto.profile.ProfileResponseDto;
import stackover.auth.service.exception.FeignRequestException;

@Component
public class ProfileServiceReactiveFallback {

    public Mono<Void> createProfile(ProfilePostDto profilePostDto) {
        return Mono.error(new FeignRequestException("Profile service unavailable. Cannot create profile."));
    }

    public Mono<ProfileResponseDto> getProfileById(Long accountId) {
        return Mono.error(new FeignRequestException("Profile service unavailable. Cannot get profile."));
    }
}