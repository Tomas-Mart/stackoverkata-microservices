package stackover.profile.service.rest.inner;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import stackover.profile.service.dto.ProfilePostDto;
import stackover.profile.service.dto.ProfileResponseDto;
import stackover.profile.service.service.ProfileService;

@RestController
@RequestMapping("/api/inner/profile")
public class ProfileInnerRestController {

    private final ProfileService profileService;

    public ProfileInnerRestController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> createProfile(@RequestBody ProfilePostDto profilePostDto) {
        return profileService.saveProfileByPostDto(profilePostDto)
                .thenReturn(ResponseEntity.ok().build());
    }

    @GetMapping
    public Mono<ResponseEntity<ProfileResponseDto>> getProfileById(@RequestParam Long accountId) {
        return profileService.getProfileResponseDtoById(accountId)
                .map(responseDto -> ResponseEntity.ok().body(responseDto));
    }
}