package br.com.fiap.mgmtmedia.amqp.producer;

import br.com.fiap.mgmtmedia.amqp.config.MessagingProperties;
import br.com.fiap.mgmtmedia.amqp.model.MediaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitProducer {

    private final MessagingProperties messagingProperties;

    private final RabbitTemplate rabbitTemplate;

    public void send(MediaMessage message) {
        log.debug("Sending message to RabbitMQ: {}", message);
        rabbitTemplate.convertAndSend(
                messagingProperties.getExchangeName(),
                messagingProperties.getRoutingKey(),
                message
        );
    }
}
