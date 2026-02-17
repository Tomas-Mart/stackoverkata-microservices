package stackover.gateway.service.enums;

import lombok.Getter;

/**
 * Перечисление для хранения URI сервисов в системе
 */
@Getter
public enum ServiceLocation {

    AUTH_SERVICE("lb://stackover-auth-service"),
    PROFILE_SERVICE("lb://stackover-profile-service"),
    RESOURCE_SERVICE("lb://stackover-resource-service");

    private final String uri;

    ServiceLocation(String uri) {
        this.uri = uri;
    }

}