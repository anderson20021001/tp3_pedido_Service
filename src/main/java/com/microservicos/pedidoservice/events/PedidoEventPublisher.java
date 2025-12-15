package com.microservicos.pedidoservice.events;

import com.microservicos.pedidoservice.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PedidoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(PedidoEvent event, String routingKey) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_PEDIDOS,
                routingKey,
                event
        );
    }
}
