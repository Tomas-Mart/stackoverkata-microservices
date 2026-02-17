package stackover.profile.service.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .defaultHeader("Authorization", "Bearer KX9pPmTqYtXuWrZv3w6z5C8F1J4H7N2B5E9R0DcGbQa");
    }
}