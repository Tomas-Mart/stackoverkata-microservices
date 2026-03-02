package stackover.email.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record EmailRequest(

        @NotBlank(message = "Email получателя не может быть пустым")
        @Email(message = "Некорректный формат email")
        String to,

        @NotBlank(message = "Тема письма не может быть пустой")
        @Size(min = 3, max = 200, message = "Тема должна быть от 3 до 200 символов")
        String subject,

        @NotBlank(message = "Текст письма не может быть пустым")
        @Size(min = 1, max = 10000, message = "Текст должен быть от 1 до 10000 символов")
        String text,

        @JsonProperty("isHtml")
        Boolean isHtml,

        String templateName,

        Map<String, Object> templateData  // ✅ Изменено с Object на Map<String, Object>
) {

    // Статические фабричные методы (заменяют Builder)
    public static EmailRequest simple(String to, String subject, String text) {
        return new EmailRequest(to, subject, text, false, null, null);
    }

    public static EmailRequest html(String to, String subject, String htmlContent) {
        return new EmailRequest(to, subject, htmlContent, true, null, null);
    }

    public static EmailRequest template(String to, String templateName, Map<String, Object> templateData) {
        return new EmailRequest(to, "Template Email", null, true, templateName, templateData);
    }

    // Компактный конструктор для валидации
    public EmailRequest {
        // Кастомная валидация
        if (isHtml != null && isHtml && templateName != null && text != null) {
            throw new IllegalArgumentException("HTML email должен содержать либо templateName, либо text, но не оба");
        }
    }

    // Геттеры автоматически создаются: to(), subject(), text(), isHtml(), templateName(), templateData()
}