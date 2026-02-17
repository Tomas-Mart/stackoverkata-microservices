package stackover.auth.service.service.impl;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.profile.ProfilePostDto;
import stackover.auth.service.dto.profile.ProfileResponseDto;
import stackover.auth.service.exception.FeignRequestException;
import stackover.auth.service.service.ProfileService;
import stackover.auth.service.web.ProfileServiceReactiveClient;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileServiceReactiveClient profileServiceReactiveClient;

    @Override
    @CircuitBreaker(name = "profileService", fallbackMethod = "createProfileFallback")
    public Mono<ResponseEntity<Void>> createProfile(ProfilePostDto profilePostDto) {
        log.debug("Creating profile for account ID: {}", profilePostDto.accountId());
        return profileServiceReactiveClient.createProfile(profilePostDto)
                .then(Mono.defer(() -> Mono.just(ResponseEntity.ok().<Void>build())))
                .onErrorMap(FeignException.class, e -> {
                    log.error("Feign error creating profile: {}", e.getMessage());
                    return new FeignRequestException("Cannot create profile", e);
                });
    }

    @Override
    @CircuitBreaker(name = "profileService", fallbackMethod = "getProfileFallback")
    public Mono<ProfileResponseDto> getProfileById(Long accountId) {
        log.debug("Getting profile for account ID: {}", accountId);
        return profileServiceReactiveClient.getProfileById(accountId)
                .onErrorMap(FeignException.class, e -> {
                    log.error("Feign error getting profile: {}", e.getMessage());
                    return new FeignRequestException("Cannot get profile", e);
                });
    }

    public Mono<ResponseEntity<Void>> createProfileFallback(ProfilePostDto profilePostDto, Exception e) {
        log.error("Fallback for profile creation for account ID: {}", profilePostDto.accountId(), e);
        return Mono.error(new FeignRequestException("Profile service unavailable. Cannot create profile.", e));
    }

    public Mono<ProfileResponseDto> getProfileFallback(Long accountId, Exception e) {
        log.error("Fallback for getting profile for account ID: {}", accountId, e);
        return Mono.error(new FeignRequestException("Profile service unavailable. Cannot get profile.", e));
    }
}