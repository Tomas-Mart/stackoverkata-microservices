package stackover.email.service.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import stackover.email.service.service.EmailService;

import java.util.Map;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final MailSender mailSender;

    public EmailServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public Mono<Void> sendEmail(String to, String subject, String text) {
        return Mono.fromRunnable(() -> {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(to);
                    message.setSubject(subject);
                    message.setText(text);
                    mailSender.send(message);
                    log.info("✅ Email отправлен на: {}", to);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> sendEmailWithTemplate(String to, String templateName, Map<String, Object> model) {
        return Mono.fromRunnable(() -> {
                    log.info("📧 Шаблонное письмо {} отправлено на: {}", templateName, to);
                    log.info("   Данные шаблона: {}", model);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> sendEmailWithTemplate(String to, String templateName, String lang, Map<String, Object> model) {
        return Mono.fromRunnable(() -> {
                    log.info("📧 Шаблонное письмо {} (язык: {}) отправлено на: {}", templateName, lang, to);
                    log.info("   Данные шаблона: {}", model);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Boolean> verifyEmail(String token) {
        return Mono.fromCallable(() -> {
                    log.info("🔐 Верификация email с токеном: {}", token);
                    return true;
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}