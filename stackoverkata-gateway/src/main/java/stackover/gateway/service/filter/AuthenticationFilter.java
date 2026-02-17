package stackover.gateway.service.filter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import stackover.gateway.service.dto.RequestData;
import stackover.gateway.service.exception.ValidateRequestException;
import stackover.gateway.service.security.JwtTokenProvider;

@Slf4j
@Component
public class AuthenticationFilter implements GatewayFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            RequestData requestData = validateRequestAndGetRequestData(exchange.getRequest());
            log.info("Аутентификация успешна для пользователя: {}", requestData.claims().getSubject());
            return chain.filter(exchange);
        } catch (ValidateRequestException e) {
            log.error("Ошибка аутентификации: {}", e.getMessage());
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    public RequestData validateRequestAndGetRequestData(ServerHttpRequest request)
            throws ValidateRequestException {
        String token = extractToken(request);
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);
        return new RequestData(token, claims);
    }

    private String extractToken(ServerHttpRequest request) throws ValidateRequestException {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ValidateRequestException("Отсутствует заголовок Authorization");
        }
        return authHeader.substring(7);
    }
}