package stackover.auth.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    public Scheduler jwtScheduler() {
        return Schedulers.boundedElastic();
    }
}