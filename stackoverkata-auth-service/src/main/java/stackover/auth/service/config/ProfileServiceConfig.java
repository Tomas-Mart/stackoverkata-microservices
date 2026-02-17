package stackover.auth.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "profile.service")
public record ProfileServiceConfig(String url) {
}