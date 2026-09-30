package com.corhuila.edutrack.academic.infrastructure.persistence;

import com.corhuila.edutrack.academic.domain.exception.OutboxSerializationException;
import com.corhuila.edutrack.academic.domain.model.DomainEvent;
import com.corhuila.edutrack.academic.domain.model.OutboxEvent;
import com.corhuila.edutrack.academic.domain.model.OutboxEventStatus;
import com.corhuila.edutrack.academic.domain.port.out.OutboxEventPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * JPA adapter of {@link OutboxEventPort}.
 * Writes events to the {@code outbox_events} table instead of publishing them directly.
 */
@Component
public class JpaOutboxEventAdapter implements OutboxEventPort {

    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public JpaOutboxEventAdapter(OutboxEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * MANDATORY guarantees the insert joins the caller's transaction.
     * If there is none, Spring fails fast instead of silently breaking atomicity.
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void append(String aggregateType, DomainEvent event) {
        OutboxEventEntity entity = new OutboxEventEntity(
            UUID.fromString(event.getEventId()),
            aggregateType,
            UUID.fromString(event.getAggregateId()),
            event.getEventType(),
            serialize(event),
            OutboxEventStatus.PENDING,
            Instant.now()
        );
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutboxEvent> findPending(int limit) {
        return repository
            .findByStatusOrderByCreatedAtAsc(OutboxEventStatus.PENDING, PageRequest.of(0, limit))
            .stream()
            .map(this::toDomain)
            .toList();
    }

    private String serialize(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new OutboxSerializationException(
                "Could not serialize event " + event.getEventId() + " for the outbox", e);
        }
    }

    private OutboxEvent toDomain(OutboxEventEntity entity) {
        return new OutboxEvent(
            entity.getId(),
            entity.getAggregateType(),
            entity.getAggregateId(),
            entity.getEventType(),
            entity.getPayload(),
            entity.getStatus(),
            entity.getCreatedAt()
        );
    }
}
