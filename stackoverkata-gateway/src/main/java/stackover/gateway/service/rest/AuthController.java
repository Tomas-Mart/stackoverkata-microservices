package stackover.gateway.service.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import stackover.gateway.service.web.AuthServiceWebClient;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceWebClient authServiceWebClient;

    @GetMapping("/validate")
    public Mono<ResponseEntity<Void>> validateToken(@RequestHeader String authorization) {
        return authServiceWebClient.validateToken(authorization)
                .map(isValid -> isValid
                        ? ResponseEntity.ok().build()
                        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}