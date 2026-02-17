package stackover.auth.service.util.enums;

public enum ResponseMessages {
    LOGIN_FAILED_MESSAGE("Неверный логин или пароль"),
    SIGNUP_EXIST_MESSAGE("Аккаунт с таким email уже существует");

    private final String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
