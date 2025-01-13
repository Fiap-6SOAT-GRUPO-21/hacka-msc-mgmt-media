package br.com.fiap.mgmtmedia.amqp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration("messagingConfig")
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class MessagingProperties {

    private String exchangeName;

    private String queueName;

    private String routingKey;

}
