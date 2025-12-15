package com.microservicos.pedidoservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_PEDIDOS = "exchange.pedidos";
    public static final String QUEUE_PROCESSOR = "q.pedidos.processor";

    @Bean
    public TopicExchange pedidosExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_PEDIDOS).durable(true).build();
    }

    @Bean
    public Queue processorQueue() {
        return QueueBuilder.durable(QUEUE_PROCESSOR).build();
    }

    @Bean
    public Binding binding(TopicExchange pedidosExchange, Queue processorQueue) {
        return BindingBuilder.bind(processorQueue)
                .to(pedidosExchange)
                .with("pedido.#");
    }
}
