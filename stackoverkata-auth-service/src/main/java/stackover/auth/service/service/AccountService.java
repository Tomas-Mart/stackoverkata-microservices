package stackover.auth.service.service;

import reactor.core.publisher.Mono;
import stackover.auth.service.dto.account.AccountResponseDto;
import stackover.auth.service.dto.request.SignupRequestDto;
import stackover.auth.service.model.Account;

/**
 * Сервис для работы с данными аккаунтов
 */
public interface AccountService {

    Mono<AccountResponseDto> getAccountById(long id);

    Mono<Void> signup(SignupRequestDto signupRequest);

    Mono<Boolean> existsById(long id);

    Mono<Account> findById(Long id);

    Mono<Account> save(Account account);

    Mono<Long> count();

    Mono<Account> findByEmail(String email);}