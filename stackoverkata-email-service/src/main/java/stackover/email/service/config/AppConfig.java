package stackover.email.service.config;

import freemarker.template.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class AppConfig {

    @Bean(name = "freemarkerConfiguration")
    public Configuration getFreeMarkerConfiguration() {
        Configuration config = new Configuration(Configuration.VERSION_2_3_27);
        config.setClassForTemplateLoading(this.getClass(), "/mail-templates/");
        return config;
    }

}
