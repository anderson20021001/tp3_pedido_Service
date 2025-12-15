package com.microservicos.pedidoservice.events;

import com.microservicos.pedidoservice.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoEventsListener {

    @RabbitListener(queues = RabbitConfig.QUEUE_PROCESSOR)
    public void consumir(PedidoEvent event) {
        System.out.println(
                "[EVENTO RECEBIDO] Tipo: " + event.getEventType() +
                        " | Pedido ID: " + event.getAggregateId() +
                        " | Versão: " + event.getEventVersion()
        );
    }
}
