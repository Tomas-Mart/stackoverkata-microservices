package stackover.resource.service.service.dto.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.dto.response.ProfileResponseDto;
import stackover.resource.service.dto.response.UserResponseDto;
import stackover.resource.service.feign.AuthServiceClient;
import stackover.resource.service.feign.ProfileServiceClient;
import stackover.resource.service.repository.dto.UserDtoRepository;
import stackover.resource.service.service.dto.UserDtoService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDtoServiceImpl implements UserDtoService {

    private final UserDtoRepository userRepository;
    private final AuthServiceClient authServiceClient;
    private final ProfileServiceClient profileServiceClient;

    @Override
    public Mono<UserResponseDto> getUserDtoByUserId(Long userId) {
        return Mono.defer(() -> {
            log.info("Fetching user DTO for ID: {}", userId);
            return authServiceClient.isAccountExist(userId)
                    .flatMap(exists -> exists
                            ? processUserData(userId)
                            : Mono.error(new RuntimeException("Account not found for ID: " + userId))
                    )
                    .onErrorResume(e -> {
                        log.error("Error fetching user data for ID {}: {}", userId, e.getMessage());
                        return Mono.error(e);
                    });
        });
    }

    private Mono<UserResponseDto> processUserData(Long userId) {
        return userRepository.getUserDataBaseDto(userId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("User data not found in local DB for ID: {}", userId);
                    return fetchFromAuthService(userId);
                }))
                .flatMap(dbDto -> fetchProfileAndCombine(userId, dbDto));
    }

    private Mono<UserResponseDto> fetchFromAuthService(Long userId) {
        return authServiceClient.getUserById(userId)
                .flatMap(authUser -> profileServiceClient.getProfileByAccountId(userId)
                        .map(profile -> new UserResponseDto(
                                        userId,
                                        profile.email(),
                                        null, // fullName
                                        null, // imageLink
                                        null, // city
                                        0L,   // reputation
                                        null, // registrationDate
                                        0L    // votes
                                )
                        )
                );
    }

    private Mono<UserResponseDto> fetchProfileAndCombine(Long userId, UserResponseDto dbDto) {
        return profileServiceClient.getProfileByAccountId(userId)
                .map(profile -> combineData(profile, dbDto))
                .doOnSuccess(dto -> log.info("Successfully fetched combined data for ID: {}", userId));
    }

    private UserResponseDto combineData(ProfileResponseDto profile, UserResponseDto dbDto) {
        return new UserResponseDto(
                dbDto.id(),
                profile.email(),
                dbDto.fullName(),
                dbDto.linkImage(),
                dbDto.city(),
                dbDto.reputation(),
                dbDto.registrationDate(),
                dbDto.votes()
        );
    }
}