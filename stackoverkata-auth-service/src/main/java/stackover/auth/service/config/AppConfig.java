package stackover.auth.service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;
import stackover.auth.service.model.Account;
import stackover.auth.service.model.Role;
import stackover.auth.service.security.AccountReactiveUserDetailsService;
import stackover.auth.service.security.JwtAuthEntryPoint;
import stackover.auth.service.security.JwtAuthenticationConverter;
import stackover.auth.service.security.JwtTokenProvider;
import stackover.auth.service.service.AccountService;
import stackover.auth.service.service.RoleService;
import stackover.auth.service.util.enums.RoleNameEnum;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
@EnableConfigurationProperties(ProfileServiceConfig.class)
public class AppConfig {

    private final RoleService roleService;
    private final AccountService accountService;
    private final JwtTokenProvider tokenProvider;
    private final JwtAuthEntryPoint authEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter(tokenProvider);
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(
                new AccountReactiveUserDetailsService(roleService, accountService)
        );
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    @Bean
    public AuthenticationWebFilter jwtAuthenticationFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        filter.setServerAuthenticationConverter(jwtAuthenticationConverter());
        filter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        filter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(authEntryPoint));
        return filter;
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .authorizeExchange()
                .pathMatchers(
                        "/api/auth/**",
                        "/api/internal/**",
                        "/actuator/**",
                        "/favicon.ico",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                ).permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public CommandLineRunner dataInitializer(AccountService accountService) {
        return args -> {
            roleService.count()
                    .flatMap(count -> {
                        if (count == 0) {
                            return initTestData(accountService);
                        }
                        log.info("Тестовые данные уже существуют, инициализация не требуется");
                        return Mono.empty();
                    })
                    .onErrorResume(e -> {
                        log.error("Ошибка при инициализации тестовых данных", e);
                        return Mono.empty();
                    })
                    .block(); // Только для инициализации
        };
    }

    private Mono<Void> initTestData(AccountService accountService) {
        return Mono.defer(() -> {
            Role adminRole = new Role(null, RoleNameEnum.ROLE_ADMIN);
            Role userRole = new Role(null, RoleNameEnum.ROLE_USER);

            return roleService.save(adminRole)
                    .flatMap(savedAdminRole -> roleService.save(userRole)
                            .flatMap(savedUserRole -> {
                                Account admin = Account.builder()
                                        .email("admin@example.com")
                                        .password(passwordEncoder().encode("admin"))
                                        .roleId(savedAdminRole.getId())
                                        .build();

                                Account user = Account.builder()
                                        .email("user@example.com")
                                        .password(passwordEncoder().encode("user"))
                                        .roleId(savedUserRole.getId())
                                        .build();

                                return accountService.save(admin)
                                        .then(accountService.save(user));
                            })
                            .then());
        });
    }
}