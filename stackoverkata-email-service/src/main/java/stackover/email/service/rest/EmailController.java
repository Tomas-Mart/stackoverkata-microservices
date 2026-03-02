package stackover.email.service.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import stackover.email.service.dto.EmailRequest;
import stackover.email.service.service.EmailService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public Mono<Void> sendEmail(@Valid @RequestBody EmailRequest request) {
        log.info("📧 Отправка email на адрес: {}", request.to());
        return emailService.sendEmail(request.to(), request.subject(), request.text());
    }

    @PostMapping("/send/simple")
    public Mono<Void> sendSimpleEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text) {
        log.info("📧 Отправка простого email на адрес: {}", to);
        return emailService.sendEmail(to, subject, text);
    }

    @PostMapping("/send/html")
    public Mono<Void> sendHtmlEmail(@Valid @RequestBody EmailRequest request) {
        log.info("📧 Отправка HTML email на адрес: {}", request.to());
        return emailService.sendEmail(request.to(), request.subject(), request.text());
    }

    @PostMapping("/send/template")
    public Mono<Void> sendTemplateEmail(@Valid @RequestBody EmailRequest request) {
        log.info("📧 Отправка шаблонного email на адрес: {}", request.to());
        return emailService.sendEmailWithTemplate(
                request.to(),
                request.templateName(),
                request.templateData()
        );
    }

    @PostMapping("/send/template-with-lang")
    public Mono<Void> sendTemplateEmailWithLang(
            @RequestParam String to,
            @RequestParam String templateName,
            @RequestParam String lang,
            @RequestBody Map<String, Object> data) {
        log.info("📧 Отправка шаблонного email ({} язык) на адрес: {}", lang, to);
        return emailService.sendEmailWithTemplate(to, templateName, lang, data);
    }

    @GetMapping("/verify/{token}")
    public Mono<Boolean> verifyEmail(@PathVariable String token) {
        log.info("🔐 Верификация email с токеном: {}", token);
        return emailService.verifyEmail(token);
    }
}