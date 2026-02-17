package stackover.email.service.enums;

public enum MailLang {
    RU("ru"),
    EN("en");
    private final String localeName;

    MailLang(String localeName) {
        this.localeName = localeName;
    }

    public String getLocaleName() {
        return localeName;
    }
}
