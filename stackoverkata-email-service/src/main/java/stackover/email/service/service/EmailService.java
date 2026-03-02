package stackover.email.service.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface EmailService {

    /**
     * Отправка простого текстового письма
     */
    Mono<Void> sendEmail(String to, String subject, String text);

    /**
     * Отправка шаблонного письма (без указания языка)
     */
    Mono<Void> sendEmailWithTemplate(String to, String templateName, Map<String, Object> model);

    /**
     * Отправка шаблонного письма с указанием языка (ru/en)
     */
    Mono<Void> sendEmailWithTemplate(String to, String templateName, String lang, Map<String, Object> model);

    /**
     * Верификация email по токену
     */
    Mono<Boolean> verifyEmail(String token);
}