package stackover.email.service.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import stackover.email.service.service.EmailService;

import jakarta.mail.internet.MimeMessage;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final Configuration freemarkerConfiguration;

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
                    try {
                        Template template = freemarkerConfiguration.getTemplate(templateName + ".ftl");
                        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                        sendHtmlEmail(to, "Уведомление от StackOverKata", htmlContent);
                        log.info("📧 Шаблонное письмо {} отправлено на: {}", templateName, to);
                    } catch (Exception e) {
                        log.error("❌ Ошибка при отправке шаблонного письма: {}", e.getMessage());
                        throw new RuntimeException("Ошибка отправки email", e);
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> sendEmailWithTemplate(String to, String templateName, String lang, Map<String, Object> model) {
        return Mono.fromRunnable(() -> {
                    try {
                        String fullTemplatePath = lang + "/" + templateName + "_" + lang;
                        Template template = freemarkerConfiguration.getTemplate(fullTemplatePath + ".ftl");
                        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                        String subject = getSubjectByTemplateAndLang(templateName, lang);
                        sendHtmlEmail(to, subject, htmlContent);
                        log.info("📧 Шаблонное письмо {} (язык: {}) отправлено на: {}", templateName, lang, to);
                    } catch (Exception e) {
                        log.error("❌ Ошибка при отправке шаблонного письма: {}", e.getMessage());
                        throw new RuntimeException("Ошибка отправки email", e);
                    }
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

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("❌ Ошибка при отправке HTML письма: {}", e.getMessage());
            throw new RuntimeException("Ошибка отправки HTML email", e);
        }
    }

    private String getSubjectByTemplateAndLang(String templateName, String lang) {
        if ("ru".equals(lang)) {
            switch (templateName) {
                case "invite":
                    return "Приглашение в StackOverKata";
                case "new_password":
                    return "Новый пароль";
                case "recovery_password":
                    return "Восстановление пароля";
                default:
                    return "Уведомление от StackOverKata";
            }
        } else {
            switch (templateName) {
                case "invite":
                    return "Invitation to StackOverKata";
                case "new_password":
                    return "New Password";
                case "recovery_password":
                    return "Password Recovery";
                default:
                    return "Notification from StackOverKata";
            }
        }
    }
}