package br.com.fiap.mgmtmedia.amqp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final MessagingProperties messagingProperties;

    @Bean
    Queue queue() {
        return new Queue(messagingProperties.getQueueName(), true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(messagingProperties.getExchangeName());
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(messagingProperties.getRoutingKey());
    }
}
