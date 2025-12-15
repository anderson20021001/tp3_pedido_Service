package com.microservicos.pedidoservice.outbox;

import com.microservicos.pedidoservice.events.PedidoEvent;
import com.microservicos.pedidoservice.events.PedidoEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxEventPublisherJob {

    private final OutboxRepository repository;
    private final PedidoEventPublisher publisher;

    public OutboxEventPublisherJob(OutboxRepository repository,
                                   PedidoEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publicarEventosPendentes() {
        List<OutboxEvent> pendentes =
                repository.findByStatusOrderByCreatedAtAsc("PENDING");

        for (OutboxEvent out : pendentes) {
            try {
                PedidoEvent event = new PedidoEvent(
                        out.getEventType(),
                        out.getAggregateId(),
                        out.getPayload(),
                        out.getEventVersion()
                );

                publisher.publish(event, mapRoutingKey(out.getEventType()));
                out.setStatus("SENT");
                repository.save(out);

            } catch (Exception e) {
                out.setStatus("FAILED");
                repository.save(out);
            }
        }
    }

    private String mapRoutingKey(Enum<?> type) {
        return "pedido." + type.name().toLowerCase();
    }
}
