package stackover.auth.service.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.profile.ProfilePostDto;
import stackover.auth.service.dto.profile.ProfileResponseDto;

public interface ProfileService {

    Mono<ResponseEntity<Void>> createProfile(ProfilePostDto profilePostDto);

    Mono<ProfileResponseDto> getProfileById(Long accountId);
}