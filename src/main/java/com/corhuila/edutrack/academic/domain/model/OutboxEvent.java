package com.corhuila.edutrack.academic.domain.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Immutable domain view of an outbox record.
 * The payload is kept as a raw JSON string so it can be relayed to the broker as-is.
 */
public class OutboxEvent {

    private final UUID id;
    private final String aggregateType;
    private final UUID aggregateId;
    private final String eventType;
    private final String payload;
    private final OutboxEventStatus status;
    private final Instant createdAt;

    public OutboxEvent(UUID id,
                       String aggregateType,
                       UUID aggregateId,
                       String eventType,
                       String payload,
                       OutboxEventStatus status,
                       Instant createdAt) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getAggregateType() { return aggregateType; }
    public UUID getAggregateId() { return aggregateId; }
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public OutboxEventStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}