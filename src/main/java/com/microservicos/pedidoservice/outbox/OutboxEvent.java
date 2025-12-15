package com.microservicos.pedidoservice.outbox;

import com.microservicos.pedidoservice.events.EventType;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant createdAt;

    private Long aggregateId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Lob
    private String payload;

    private String status; // PENDING, SENT, FAILED

    private String eventVersion;

    public OutboxEvent() {}

    public OutboxEvent(Instant createdAt,
                       Long aggregateId,
                       EventType eventType,
                       String payload,
                       String status) {
        this.createdAt = createdAt;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
        this.eventVersion = "1.0";
    }

    public Long getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public Long getAggregateId() { return aggregateId; }
    public EventType getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public String getStatus() { return status; }
    public String getEventVersion() { return eventVersion; }

    public void setStatus(String status) { this.status = status; }
}
