package stackover.email.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MailConfig {

    @Bean
    @Primary
    public MailSender mailSender() {
        return new MailSender() {
            @Override
            public void send(SimpleMailMessage... simpleMessages) {
                for (SimpleMailMessage message : simpleMessages) {
                    log.info("📧 [ФЕЙК] Email отправлен на: {}", message.getTo()[0]);
                    log.info("   Тема: {}", message.getSubject());
                    log.info("   Текст: {}", message.getText());
                }
            }

            @Override
            public void send(SimpleMailMessage simpleMessage) {
                log.info("📧 [ФЕЙК] Email отправлен на: {}", simpleMessage.getTo()[0]);
                log.info("   Тема: {}", simpleMessage.getSubject());
                log.info("   Текст: {}", simpleMessage.getText());
            }
        };
    }
}