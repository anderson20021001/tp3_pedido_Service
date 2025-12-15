package com.microservicos.pedidoservice.events;

import java.time.Instant;
import java.util.UUID;

public class PedidoEvent {

    private String eventId;
    private EventType eventType;
    private Instant occurredAt;
    private Long aggregateId;
    private String payload; // JSON
    private String eventVersion;

    public PedidoEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
        this.eventVersion = "1.0";
    }

    public PedidoEvent(EventType eventType,
                       Long aggregateId,
                       String payload,
                       String eventVersion) {
        this();
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.eventVersion = eventVersion;
    }

    public String getEventId() { return eventId; }
    public EventType getEventType() { return eventType; }
    public Instant getOccurredAt() { return occurredAt; }
    public Long getAggregateId() { return aggregateId; }
    public String getPayload() { return payload; }
    public String getEventVersion() { return eventVersion; }
}
