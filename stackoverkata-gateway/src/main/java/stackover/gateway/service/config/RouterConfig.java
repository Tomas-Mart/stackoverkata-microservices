package stackover.gateway.service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import stackover.gateway.service.filter.AuthenticationFilter;

@Configuration
public class RouterConfig {

    private final AuthenticationFilter authenticationFilter;

    public RouterConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("stackover-auth-service",
                        r -> r.path("/api/auth/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://stackover-auth-service"))
                .route("stackover-profile-service",
                        r -> r.path("/api/profile/**")
                                .filters(f -> f.stripPrefix(1)
                                        .filter(authenticationFilter))
                                .uri("lb://stackover-profile-service"))
                .route("stackover-resource-service",
                        r -> r.path("/api/resource/**")
                                .filters(f -> f.stripPrefix(1)
                                        .filter(authenticationFilter))
                                .uri("lb://stackover-resource-service"))
                .build();
    }
}