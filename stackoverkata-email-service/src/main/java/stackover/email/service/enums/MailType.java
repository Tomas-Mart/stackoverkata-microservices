package stackover.email.service.enums;

public enum MailType {
    /**
     * Поле templateName хранит имя используемого темплейта для этого типа письма
     */
    INVITE("invite"),
    NEW_PASSWORD("new_password"),
    RECOVERY_PASSWORD("recovery_password");
    private final String templateName;

    MailType(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }
}
