package stackover.email.service.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RabbitMQConstants {

    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_EXCHANGE = "email.exchange";
    public static final String EMAIL_ROUTING_KEY = "email.routing.key";
}