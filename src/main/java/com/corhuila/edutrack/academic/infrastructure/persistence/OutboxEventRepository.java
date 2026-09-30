package com.corhuila.edutrack.academic.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.corhuila.edutrack.academic.domain.model.OutboxEventStatus;

/**
 * Spring Data repository for outbox records.
 */
@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, UUID> {

    /**
     * Finds events by status, oldest first, to preserve publishing order.
     * Backed by the partial index {@code idx_outbox_events_pending}.
     * Use {@code PageRequest.of(0, batchSize)} to bound the batch size.
     */
    List<OutboxEventEntity> findByStatusOrderByCreatedAtAsc(OutboxEventStatus status, Pageable pageable);
}
