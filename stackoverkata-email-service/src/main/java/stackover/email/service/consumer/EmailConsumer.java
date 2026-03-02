package stackover.email.service.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import stackover.email.service.service.EmailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "email.queue")
    public void handleEmailMessage(String message) {
        log.info("📨 Получено сообщение из RabbitMQ: {}", message);

        try {
            // Парсим JSON сообщение
            JsonNode json = objectMapper.readTree(message);

            String to = json.get("to").asText();
            String subject = json.get("subject").asText();
            String body = json.get("body").asText();

            log.debug("📧 Параметры письма: to={}, subject={}", to, subject);

            // Отправляем email
            emailService.sendEmail(to, subject, body);

            log.info("✅ Письмо успешно отправлено на адрес: {}", to);

        } catch (NullPointerException e) {
            log.error("❌ Ошибка: отсутствует обязательное поле в JSON: {}", e.getMessage());
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.error("❌ Ошибка парсинга JSON: {}", e.getMessage());
        } catch (Exception e) {
            log.error("❌ Непредвиденная ошибка при обработке сообщения: {}", e.getMessage());
        }
    }
}