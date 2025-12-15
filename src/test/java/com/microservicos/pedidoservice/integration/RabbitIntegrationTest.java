package com.microservicos.pedidoservice.integration;

import com.microservicos.pedidoservice.PedidoServiceApplication;
import com.microservicos.pedidoservice.model.Pedido;
import com.microservicos.pedidoservice.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PedidoServiceApplication.class)
@ExtendWith(SpringExtension.class)
@Testcontainers
@ActiveProfiles("test") // cria um perfil de teste separado
public class RabbitIntegrationTest {

    @Container
    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.9-management")
            .withExposedPorts(5672, 15672);

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    void criarPedido_publicaEvento() {
        // Cria pedido no H2
        Pedido pedido = new Pedido("Pedido Teste", 123.45);
        pedidoRepository.save(pedido);

        // Publica evento no RabbitMQ
        amqpTemplate.convertAndSend(
                "pedido.exchange", // exchange
                "pedido.routingkey", // routing key
                pedido
        );

        // Verifica se o pedido foi persistido
        Pedido encontrado = pedidoRepository.findById(pedido.getId()).orElse(null);
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getDescricao()).isEqualTo("Pedido Teste");
        assertThat(encontrado.getValor()).isEqualTo(123.45);
    }
}
