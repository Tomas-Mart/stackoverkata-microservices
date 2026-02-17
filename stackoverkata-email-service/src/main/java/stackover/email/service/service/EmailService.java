package stackover.email.service.service;

import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<Void> sendEmail(String to, String subject, String text);
    Mono<Void> sendEmailWithTemplate(String to, String template, Object data);
    Mono<Boolean> verifyEmail(String token);
}