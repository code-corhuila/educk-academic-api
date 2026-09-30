package com.corhuila.edutrack.academic.domain.port.out;

import com.corhuila.edutrack.academic.domain.model.DomainEvent;
import com.corhuila.edutrack.academic.domain.model.OutboxEvent;

import java.util.List;

/**
 * Outbound port for the Transactional Outbox pattern (ADR-007).
 */
public interface OutboxEventPort {

    /**
     * Stores the event as PENDING in the outbox.
     * Must be invoked inside the same transaction that persists the business entity.
     *
     * @param aggregateType logical aggregate name, e.g. "Grade"
     * @param event         domain event to store
     */
    void append(String aggregateType, DomainEvent event);

    /**
     * Returns the oldest PENDING events first, limited to {@code limit} items.
     */
    List<OutboxEvent> findPending(int limit);
}