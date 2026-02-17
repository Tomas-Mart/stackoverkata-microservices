package stackover.email.service.factory;

import stackover.email.service.enums.MailLang;
import stackover.email.service.enums.MailType;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MailTemplateFactory {
    private final Configuration freemarkerConfiguration;

    /**
     * @param mailType вид письма
     * @param mailLang язык обращения к пользователю
     * @return ftl-шаблон письма
     * @throws IOException       ошибка парсинга шаблона - не найден ftl-файл
     * @throws TemplateException ошибка парсинга шаблона - не удалось обработать ftl-файл
     */
    public Template getTemplate(@NotNull MailType mailType, @NotNull MailLang mailLang) throws IOException, TemplateException {
        return freemarkerConfiguration.getTemplate((mailLang.getLocaleName() + "/" + mailType.getTemplateName() + "_" + mailLang.getLocaleName() + ".ftl"));
    }
}
