package stackover.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import stackover.auth.service.security.JwtTokenProvider;

import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceApplicationTests {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        when(jwtTokenProvider.getRefreshExpirationMs()).thenReturn(Mono.just(2592000000L));
        when(jwtTokenProvider.getAccessExpirationSec()).thenReturn(Mono.just(86400L));
    }

    @Test
    void contextLoads() {
    }
}