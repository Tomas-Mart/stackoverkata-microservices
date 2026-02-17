package stackover.auth.service.rest.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import stackover.auth.service.exception.AccountExistException;
import stackover.auth.service.exception.AccountNotAvailableException;
import stackover.auth.service.exception.EntityNotFoundException;
import stackover.auth.service.exception.FeignRequestException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class AdviceRestController {

    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<Object>> handleEntityNotFound(EntityNotFoundException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, exchange);
    }

    @ExceptionHandler(AccountExistException.class)
    public Mono<ResponseEntity<Object>> handleAccountExist(AccountExistException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, exchange);
    }

    @ExceptionHandler(AccountNotAvailableException.class)
    public Mono<ResponseEntity<Object>> handleAccountNotAvailable(AccountNotAvailableException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, exchange);
    }

    @ExceptionHandler(FeignRequestException.class)
    public Mono<ResponseEntity<Object>> handleFeignRequestException(FeignRequestException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, exchange);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Object>> handleAllUncaughtException(Exception ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, exchange);
    }

    private Mono<ResponseEntity<Object>> buildErrorResponse(HttpStatus status, Exception ex, ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", status.value());
            body.put("error", status.getReasonPhrase());
            body.put("message", ex.getMessage());
            body.put("path", exchange.getRequest().getPath().value());
            return ResponseEntity.status(status).body(body);
        });
    }
}