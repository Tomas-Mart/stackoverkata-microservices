package stackover.auth.service.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

/**
 * Конфигурация для Feign клиентов
 */
@Configuration
public class FeignConfig {

    /**
     * Настройка уровня логирования Feign (FULL - максимальная детализация)
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Интерцептор для добавления:
     * 1. JWT токена в заголовки (если пользователь аутентифицирован)
     * 2. Content-Type: application/json для всех запросов
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Добавляем Content-Type
            requestTemplate.header("Content-Type", "application/json");

            // Добавляем JWT токен при наличии
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getCredentials() != null) {
                requestTemplate.header("Authorization",
                        "Bearer " + authentication.getCredentials().toString());
            }
        };
    }

    /**
     * Кастомный обработчик ошибок Feign клиентов с локализованными сообщениями
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) ->
                switch (response.status()) {
                    case 400 -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Неверный запрос к сервису: " + methodKey);
                    case 404 -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Ресурс не найден: " + methodKey);
                    case 503 -> new ResponseStatusException(
                            HttpStatus.SERVICE_UNAVAILABLE,
                            "Сервис временно недоступен");
                    default -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Ошибка при вызове Feign клиента");
                };
    }
}