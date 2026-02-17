package stackover.gateway.service.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GatewayInfoContributor implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("gateway", Map.of(
                "status", "active",
                "reactor", "enabled",
                "r2dbc", "disabled" // или enabled, если используете R2DBC
        ));
    }
}