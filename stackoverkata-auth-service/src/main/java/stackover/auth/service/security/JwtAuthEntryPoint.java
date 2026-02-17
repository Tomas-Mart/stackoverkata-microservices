package stackover.auth.service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Реактивный компонент для обработки ошибок аутентификации
 */
@Slf4j
@Component
public class JwtAuthEntryPoint implements ServerAuthenticationEntryPoint {

    /**
     * Метод вызывается при попытке доступа к защищенному ресурсу без аутентификации
     *
     * @param exchange ServerWebExchange
     * @param ex       Исключение аутентификации
     * @return Mono<Void>
     */
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return Mono.fromRunnable(() -> {
                    log.error("Ошибка аутентификации: {}", ex.getMessage());
                    log.debug("Запрос: {} {} вызвал ошибку аутентификации",
                            exchange.getRequest().getMethod(), exchange.getRequest().getPath());
                })
                .then(Mono.defer(() -> {
                    // Формируем JSON ответ с описанием ошибки
                    String jsonResponse = String.format(
                            "{\"status\": \"error\", \"message\": \"%s\", \"path\": \"%s\"}",
                            "Требуется аутентификация",
                            exchange.getRequest().getPath().toString()
                    );

                    // Создаем DataBuffer с JSON ответом
                    byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

                    // Устанавливаем заголовки и статус ответа
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    log.info("Отправлен ответ 401 UNAUTHORIZED для запроса: {}", exchange.getRequest().getPath());

                    // Записываем ответ
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                }));
    }
}