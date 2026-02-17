package stackover.auth.service.rest.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.account.AccountResponseDto;
import stackover.auth.service.exception.AccountNotAvailableException;
import stackover.auth.service.exception.EntityNotFoundException;
import stackover.auth.service.exception.FeignRequestException;
import stackover.auth.service.service.AccountService;
import stackover.auth.service.service.RoleService;
import stackover.auth.service.util.enums.RoleNameEnum;

@Slf4j
@RestController
@RequestMapping("api/internal/account")
@Tag(name = "Аккаунты", description = "API для работы с аккаунтами пользователей")
@RequiredArgsConstructor
public class AccountInnerRestController {

    private final AccountService accountService;
    private final RoleService roleService;

    @GetMapping("/{id}")
    @Operation(summary = "Получение аккаунта по ID",
            description = "Возвращает полную информацию об аккаунте по его идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аккаунт успешно найден",
                            content = @Content(schema = @Schema(implementation = AccountResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Аккаунт не найден",
                            content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = FeignRequestException.class)))})
    public Mono<AccountResponseDto> getAccountById(@PathVariable long id) {
        return Mono.just(id)
                .doOnNext(i -> log.info("Запрос информации об аккаунте с ID: {}", i))
                .flatMap(accountService::getAccountById)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Аккаунт с ID {} не найден", id);
                    return Mono.error(new EntityNotFoundException("Аккаунт с ID " + id + " не найден"));
                }))
                .doOnSuccess(__ -> log.debug("Информация об аккаунте с ID {} успешно получена", id));
    }

    @GetMapping("/{id}/exists-with-role")
    @Operation(summary = "Проверка существования аккаунта с ролью",
            description = "Проверяет существование аккаунта с указанным ID и ролью",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Проверка выполнена успешно",
                            content = @Content(schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                            content = @Content(schema = @Schema(implementation = AccountNotAvailableException.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = FeignRequestException.class)))})
    public Mono<ResponseEntity<Boolean>> checkExistByIdAndRole(
            @PathVariable long id,
            @RequestParam @Parameter(description = "Роль пользователя", required = true, example = "USER") RoleNameEnum role) {
        return Mono.just(id)
                .doOnNext(i -> log.info("Проверка существования аккаунта с ID {} и ролью {}", i, role))
                .flatMap(i -> roleService.checkExistByIdAndRole(i, role))
                .map(ResponseEntity::ok)
                .doOnSuccess(exists -> log.debug("Результат проверки аккаунта с ID {} и ролью {}: {}", id, role, exists));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Проверка существования аккаунта",
            description = "Проверяет существование аккаунта с указанным ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Проверка выполнена успешно",
                            content = @Content(schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "404", description = "Аккаунт не найден",
                            content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = FeignRequestException.class)))})
    public Mono<ResponseEntity<Boolean>> doesAccountExist(@PathVariable long id) {
        return Mono.just(id)
                .doOnNext(i -> log.info("Проверка существования аккаунта с ID {}", i))
                .flatMap(accountService::existsById)
                .map(ResponseEntity::ok)
                .doOnSuccess(exists -> log.debug("Результат проверки аккаунта с ID {}: {}", id, exists));
    }
}