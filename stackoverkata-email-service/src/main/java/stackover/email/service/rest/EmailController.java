package stackover.email.service.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import stackover.email.service.dto.EmailRequest;
import stackover.email.service.service.EmailService;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public Mono<Void> sendEmail(@RequestBody EmailRequest request) {
        // Для record используем методы to(), subject(), text() - они создаются автоматически!
        return emailService.sendEmail(request.to(), request.subject(), request.text());
    }

    @PostMapping("/send/simple")
    public Mono<Void> sendSimpleEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text) {
        return emailService.sendEmail(to, subject, text);
    }

    @PostMapping("/send/html")
    public Mono<Void> sendHtmlEmail(@RequestBody EmailRequest request) {
        // Используем html метод
        if (Boolean.TRUE.equals(request.isHtml())) {
            return emailService.sendEmail(request.to(), request.subject(), request.text());
        }
        return Mono.error(new IllegalArgumentException("Требуется HTML email"));
    }

    @PostMapping("/send/template")
    public Mono<Void> sendTemplateEmail(@RequestBody EmailRequest request) {
        if (request.templateName() != null) {
            return emailService.sendEmailWithTemplate(
                    request.to(),
                    request.templateName(),
                    request.templateData()
            );
        }
        return Mono.error(new IllegalArgumentException("Требуется templateName"));
    }

    @GetMapping("/verify/{token}")
    public Mono<Boolean> verifyEmail(@PathVariable String token) {
        return emailService.verifyEmail(token);
    }
}