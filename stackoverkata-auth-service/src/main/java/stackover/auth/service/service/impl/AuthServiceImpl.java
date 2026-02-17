package stackover.auth.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.request.LoginRequestDto;
import stackover.auth.service.dto.response.AuthResponseDto;
import stackover.auth.service.dto.profile.ProfileResponseDto;
import stackover.auth.service.exception.EntityNotFoundException;
import stackover.auth.service.model.Account;
import stackover.auth.service.security.JwtTokenProvider;
import stackover.auth.service.service.AuthService;
import stackover.auth.service.service.ProfileService;
import stackover.auth.service.service.RefreshTokenService;
import stackover.auth.service.service.RoleService;
import stackover.auth.service.util.enums.RoleNameEnum;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RoleService roleService;
    private final ProfileService profileService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    @Override
    @Transactional
    public Mono<AuthResponseDto> login(LoginRequestDto loginRequestDto) {
        log.info("Попытка аутентификации для email: {}", loginRequestDto.email());

        return authenticateUser(loginRequestDto)
                .flatMap(this::processSuccessfulAuthentication)
                .doOnSuccess(authResponse -> log.info("Успешная аутентификация для accountId: {}", authResponse.accountId()))
                .onErrorResume(this::handleAuthenticationError);
    }

    private Mono<Account> authenticateUser(LoginRequestDto loginRequestDto) {
        return reactiveAuthenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDto.email(),
                                loginRequestDto.password()
                        ))
                .map(auth -> (Account) auth.getPrincipal());
    }

    private Mono<AuthResponseDto> processSuccessfulAuthentication(Account account) {
        return profileService.getProfileById(account.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Профиль не найден")))
                .flatMap(profile -> generateAuthResponse(account, profile));
    }

    private Mono<AuthResponseDto> generateAuthResponse(Account account, ProfileResponseDto profile) {
        return roleService.getRoleById(account.getRoleId())
                .defaultIfEmpty(RoleNameEnum.ROLE_USER)
                .flatMap(roleName -> jwtTokenProvider.generateAccessToken(account.getId(), roleName.name())
                        .flatMap(token -> jwtTokenProvider.getAccessExpirationSec()
                                .flatMap(expiresIn -> refreshTokenService.generateRefreshTokenCookie(account.getId())
                                        .then(buildAuthResponse(profile, token, expiresIn))
                                )
                        )
                );
    }

    private Mono<AuthResponseDto> buildAuthResponse(ProfileResponseDto profile, String token, Long expiresIn) {
        return Mono.just(AuthResponseDto.builder()
                .accountId(profile.accountId())
                .email(profile.email())
                .accessToken(token)
                .expiresIn(expiresIn)
                .build());
    }

    private Mono<AuthResponseDto> handleAuthenticationError(Throwable error) {
        log.error("Ошибка при аутентификации: {}", error.getMessage());
        return Mono.error(new RuntimeException("Ошибка аутентификации", error));
    }
}