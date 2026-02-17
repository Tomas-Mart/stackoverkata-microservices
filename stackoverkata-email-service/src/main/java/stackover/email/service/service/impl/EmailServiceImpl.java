package stackover.email.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import stackover.email.service.service.EmailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final MailSender mailSender;

    @Override
    public Mono<Void> sendEmail(String to, String subject, String text) {
        return Mono.fromRunnable(() -> {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(to);
                    message.setSubject(subject);
                    message.setText(text);
                    mailSender.send(message);
                    log.info("Email sent to: {}", to);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> sendEmailWithTemplate(String to, String template, Object data) {
        return Mono.fromRunnable(() -> {
                    // Здесь будет рендеринг шаблона и отправка
                    log.info("Template email sent to: {} with template: {}", to, template);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Boolean> verifyEmail(String token) {
        return Mono.fromCallable(() -> {
                    // Верификация email
                    return true;
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
