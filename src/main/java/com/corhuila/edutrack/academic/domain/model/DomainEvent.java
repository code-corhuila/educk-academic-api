package com.corhuila.edutrack.academic.domain.model;

/**
 * Minimal contract that every domain event must fulfil
 * in order to be stored in the transactional outbox (ADR-007).
 */
public interface DomainEvent {

    /** Unique event identifier. Also used as idempotency key by consumers. */
    String getEventId();

    /** Logical event name, e.g. "GradeCreated". */
    String getEventType();

    /** Identifier of the aggregate that produced the event. */
    String getAggregateId();
}