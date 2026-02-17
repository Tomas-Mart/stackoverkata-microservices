package stackover.auth.service.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.account.AccountResponseDto;
import stackover.auth.service.dto.profile.ProfilePostDto;
import stackover.auth.service.dto.request.SignupRequestDto;
import stackover.auth.service.exception.AccountExistException;
import stackover.auth.service.exception.AccountNotAvailableException;
import stackover.auth.service.exception.EntityNotFoundException;
import stackover.auth.service.exception.FeignRequestException;
import stackover.auth.service.model.Account;
import stackover.auth.service.model.Role;
import stackover.auth.service.repository.AccountRepository;
import stackover.auth.service.service.AccountService;
import stackover.auth.service.service.ProfileService;
import stackover.auth.service.service.RoleService;
import stackover.auth.service.util.enums.RoleNameEnum;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final RoleService roleService;
    private final ProfileService profileService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public Mono<Void> signup(SignupRequestDto signupRequest) {
        Objects.requireNonNull(signupRequest, "SignupRequestDto cannot be null");
        log.info("Starting registration for email: {}", signupRequest.email());

        return validateEmailUniqueness(signupRequest.email())
                .then(determineUserRole(signupRequest.roleName()))
                .flatMap(roleEntity -> createAndSaveAccount(signupRequest, roleEntity))
                .flatMap(account -> createUserProfile(signupRequest, account))
                .doOnSuccess(v -> log.info("Successful registration for email: {}", signupRequest.email()))
                .onErrorMap(FeignException.class, e -> {
                    log.error("Error creating profile. Status: {}", e.status());
                    return new FeignRequestException("Error creating profile");
                })
                .onErrorResume(e -> {
                    log.error("Unexpected error during registration", e);
                    return Mono.error(new RuntimeException("Registration error", e));
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Boolean> existsById(long id) {
        log.debug("Checking account existence with ID: {}", id);
        return accountRepository.existsById(id)
                .doOnSuccess(exists -> log.debug("Account existence check result for ID {}: {}", id, exists))
                .onErrorResume(e -> {
                    log.error("Error checking account existence with ID {}: {}", id, e.getMessage());
                    return Mono.error(new AccountExistException("Failed to check account existence"));
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Account> findById(Long id) {
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Account not found with id: " + id)))
                .onErrorResume(e -> Mono.error(new AccountNotAvailableException("Failed to retrieve account")));
    }

    @Override
    @Transactional
    public Mono<Account> save(Account account) {
        return accountRepository.save(account)
                .onErrorResume(e -> Mono.error(new AccountExistException("Failed to save account: " + e.getMessage())));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Long> count() {
        return accountRepository.count()
                .onErrorResume(e -> Mono.error(new AccountExistException("Failed to count accounts")));
    }

    @Override
    public Mono<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Account not found with email: " + email)));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AccountResponseDto> getAccountById(long id) {
        log.debug("Getting account by ID: {}", id);
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Account not found with id: " + id)))
                .flatMap(account -> roleService.getRoleById(account.getRoleId())
                        .map(role -> convertToDto(account, role)))
                .doOnSuccess(account -> log.debug("Found account with ID: {}", id))
                .onErrorResume(e -> {
                    log.error("Error getting account by ID {}: {}", id, e.getMessage());
                    return Mono.error(new AccountNotAvailableException("Failed to get account details"));
                });
    }

    private AccountResponseDto convertToDto(Account account, RoleNameEnum role) {
        return new AccountResponseDto(
                account.getId(),
                account.getEmail(),
                role,
                account.getEnabled() != null ? account.getEnabled() : true
        );
    }

    private Mono<Void> validateEmailUniqueness(String email) {
        log.debug("Checking email uniqueness: {}", email);
        return accountRepository.findByEmail(email)
                .flatMap(account -> {
                    log.warn("Existing account found with email: {}", email);
                    return Mono.error(new AccountExistException("Account with email " + email + " already exists"));
                })
                .then();
    }

    private Mono<Role> determineUserRole(String roleName) {
        log.debug("Determining role: {}", roleName);
        RoleNameEnum role = roleName != null
                ? RoleNameEnum.fromString(roleName)
                : RoleNameEnum.ROLE_USER;

        return roleService.findByName(role)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Role {} not found", role);
                    return Mono.error(new EntityNotFoundException("Role " + role + " not found"));
                }));
    }

    private Mono<Account> createAndSaveAccount(SignupRequestDto signupRequest, Role role) {
        log.debug("Creating account for email: {}", signupRequest.email());

        Account account = Account.builder()
                .email(signupRequest.email())
                .password(passwordEncoder.encode(signupRequest.password()))
                .roleId(role != null ? role.getId() : null)
                .localeTag("ru")
                .build();

        return accountRepository.save(account)
                .doOnSuccess(a -> log.debug("Account saved: {}", a.getId()))
                .doOnError(e -> log.error("Error saving account", e));
    }

    private Mono<Void> createUserProfile(SignupRequestDto signupRequest, Account account) {
        return Mono.fromCallable(() -> {
                    log.debug("Creating profile for accountId: {}", account.getId());
                    return ProfilePostDto.builder()
                            .accountId(account.getId())
                            .email(account.getEmail())
                            .fullName(signupRequest.fullName())
                            .city(signupRequest.city())
                            .persistDateTime(LocalDateTime.now())
                            .linkSite(signupRequest.linkSite())
                            .linkGitHub(signupRequest.linkGitHub())
                            .linkVk(signupRequest.linkVk())
                            .about(signupRequest.about())
                            .imageLink(signupRequest.imageLink())
                            .nickname(signupRequest.nickname())
                            .build();
                })
                .flatMap(profileService::createProfile)
                .doOnSuccess(response -> {
                    if (!response.getStatusCode().is2xxSuccessful()) {
                        throw new FeignRequestException("Failed to create profile");
                    }
                })
                .then();
    }
}