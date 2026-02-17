package stackover.auth.service.service;

import org.springframework.http.ResponseCookie;
import reactor.core.publisher.Mono;
import stackover.auth.service.dto.request.LoginRequestDto;
import stackover.auth.service.dto.response.AuthResponseDto;

public interface AuthService {

    Mono<AuthResponseDto> login(LoginRequestDto loginRequestDto);
}