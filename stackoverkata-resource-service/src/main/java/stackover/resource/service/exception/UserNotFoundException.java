package stackover.resource.service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
        log.warn("UserNotFoundException: {}", message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        log.warn("UserNotFoundException: {} - Cause: {}", message, cause.getMessage());
    }

    public UserNotFoundException(Long userId) {
        super(String.format("Пользователь с id %d не найден", userId));
        log.warn("Пользователь с id {} не найден", userId);
    }

    public UserNotFoundException(String email, String type) {
        super(String.format("Пользователь с %s '%s' не найден", type, email));
        log.warn("Пользователь с {} '{}' не найден", type, email);
    }
}