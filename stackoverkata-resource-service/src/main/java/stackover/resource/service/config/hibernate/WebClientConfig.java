package stackover.resource.service.config.hibernate;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.netty.http.client.HttpClient;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(Duration.ofSeconds(5))
                ))
                .build();
    }

    @Bean
    public Sender rabbitSender() {
        ConnectionFactory rabbitConnectionFactory = new ConnectionFactory();
        rabbitConnectionFactory.setHost("localhost");
        rabbitConnectionFactory.setPort(5672);
        rabbitConnectionFactory.setUsername("guest");
        rabbitConnectionFactory.setPassword("guest");

        return RabbitFlux.createSender(new SenderOptions()
                .connectionFactory(rabbitConnectionFactory));
    }

    @Bean
    public Receiver rabbitReceiver() {
        // 1. Явно создаём фабрику подключений RabbitMQ клиента
        com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory =
                new com.rabbitmq.client.ConnectionFactory();

        // 2. Настраиваем подключение
        rabbitConnectionFactory.setHost("localhost");
        rabbitConnectionFactory.setPort(5672);
        rabbitConnectionFactory.setUsername("guest");
        rabbitConnectionFactory.setPassword("guest");

        // 3. Создаём Receiver с явным приведением типа
        return RabbitFlux.createReceiver(new reactor.rabbitmq.ReceiverOptions()
                .connectionFactory(rabbitConnectionFactory));
    }

    @Bean
    public KafkaReceiver<String, String> kafkaReceiver() {
        return KafkaReceiver.create(kafkaReceiverOptions());
    }

    @Bean
    public ReceiverOptions<String, String> kafkaReceiverOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return ReceiverOptions.<String, String>create(props)
                .subscription(Collections.singleton("topic"));
    }
}