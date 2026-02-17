package stackover.resource.service.rest.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import stackover.resource.service.exception.AccountExistException;
import stackover.resource.service.exception.AccountNotAvailableException;
import stackover.resource.service.exception.AnswerException;
import stackover.resource.service.exception.ApiRequestException;
import stackover.resource.service.exception.CommentAnswerException;
import stackover.resource.service.exception.ConstrainException;
import stackover.resource.service.exception.FeignRequestException;
import stackover.resource.service.exception.QuestionException;
import stackover.resource.service.exception.TagAlreadyExistsException;
import stackover.resource.service.exception.TagNotFoundException;
import stackover.resource.service.exception.UserNotFoundException;
import stackover.resource.service.exception.VoteException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class AdviceRestController {

    @ExceptionHandler(AccountExistException.class)
    public Mono<ResponseEntity<Object>> handleAccountExist(AccountExistException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, exchange);
    }

    @ExceptionHandler(AccountNotAvailableException.class)
    public Mono<ResponseEntity<Object>> handleAccountNotAvailable(AccountNotAvailableException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, exchange);
    }

    @ExceptionHandler(AnswerException.class)
    public Mono<ResponseEntity<Object>> handleAnswerException(AnswerException ex, ServerWebExchange exchange) {
        HttpStatus status = "Ответ не найден".equals(ex.getMessage())
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;
        return buildErrorResponse(status, ex, exchange);
    }

    @ExceptionHandler(ApiRequestException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleApiRequestException(ApiRequestException ex) {
        return Mono.just(ResponseEntity.badRequest()
                .body(Map.of("message", ex.getErrorMessage())));
    }

    @ExceptionHandler(CommentAnswerException.class)
    public Mono<ResponseEntity<Object>> handleCommentAnswerException(CommentAnswerException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, exchange);
    }

    @ExceptionHandler(ConstrainException.class)
    public Mono<ResponseEntity<Object>> handleConstrainException(ConstrainException ex, ServerWebExchange exchange) {
        String errorMessage = ex.getConstraintViolations() != null && !ex.getConstraintViolations().isEmpty()
                ? ex.getConstraintViolations().iterator().next().getMessage()
                : ex.getMessage();

        HttpStatus status = ex.getStatus() != null
                ? ex.getStatus()
                : HttpStatus.BAD_REQUEST;

        return buildErrorResponse(status, errorMessage, exchange);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex, ServerWebExchange exchange) {
        String message = ex.getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Невалидные параметры запроса");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, exchange);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<Object>> handleConstraintViolation(ConstraintViolationException ex, ServerWebExchange exchange) {
        String message = ex.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Невалидные параметры запроса");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, exchange);
    }

    @ExceptionHandler(FeignRequestException.class)
    public Mono<ResponseEntity<Object>> handleFeignRequestException(FeignRequestException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, exchange);
    }

    @ExceptionHandler(QuestionException.class)
    public Mono<ResponseEntity<Object>> handleQuestionException(QuestionException ex, ServerWebExchange exchange) {
        HttpStatus status = "Вопрос не найден".equals(ex.getMessage())
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;
        return buildErrorResponse(status, ex, exchange);
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    public Mono<ResponseEntity<Object>> handleTagAlreadyExists(TagAlreadyExistsException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, exchange);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public Mono<ResponseEntity<Object>> handleTagNotFound(TagNotFoundException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, exchange);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<Object>> handleUserNotFound(UserNotFoundException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, exchange);
    }

    @ExceptionHandler(VoteException.class)
    public Mono<ResponseEntity<Object>> handleVoteException(VoteException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, exchange);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Object>> handleIllegalArgumentException(IllegalArgumentException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, exchange);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Object>> handleAllUncaughtException(Exception ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, exchange);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, ServerWebExchange exchange) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Невалидные параметры запроса");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, exchange);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<String>> handleWebInputException(ServerWebInputException ex) {
        log.warn("Ошибка валидации входных данных: {}", ex.getMessage());
        return Mono.just(ResponseEntity.badRequest().body("Неверный формат данных"));
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

    private Mono<ResponseEntity<Object>> buildErrorResponse(HttpStatus status, String message, ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", status.value());
            body.put("error", status.getReasonPhrase());
            body.put("message", message);
            body.put("path", exchange.getRequest().getPath().value());
            return ResponseEntity.status(status).body(body);
        });
    }
}