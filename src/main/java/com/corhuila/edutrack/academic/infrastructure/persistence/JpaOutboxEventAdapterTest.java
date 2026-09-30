package com.corhuila.edutrack.academic.infrastructure.persistence;

import com.corhuila.edutrack.academic.domain.model.GradeCreatedEvent;
import com.corhuila.edutrack.academic.domain.model.OutboxEvent;
import com.corhuila.edutrack.academic.domain.model.OutboxEventStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaOutboxEventAdapterTest {

    @Mock
    private OutboxEventRepository repository;

    private JpaOutboxEventAdapter adapter;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        adapter = new JpaOutboxEventAdapter(repository, objectMapper);
    }

    @Test
    void append_shouldPersistPendingEntityWithSerializedPayload() {
        String gradeId = UUID.randomUUID().toString();
        GradeCreatedEvent event = new GradeCreatedEvent(gradeId,
            new GradeCreatedEvent.GradeCreatedPayload(
                gradeId, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 4.5));

        adapter.append("Grade", event);

        ArgumentCaptor<OutboxEventEntity> captor = ArgumentCaptor.forClass(OutboxEventEntity.class);
        verify(repository).save(captor.capture());
        OutboxEventEntity saved = captor.getValue();

        assertEquals(UUID.fromString(event.getEventId()), saved.getId());
        assertEquals("Grade", saved.getAggregateType());
        assertEquals(UUID.fromString(gradeId), saved.getAggregateId());
        assertEquals(OutboxEventStatus.PENDING, saved.getStatus());
        assertTrue(saved.getPayload().contains("\"eventType\":\"GradeCreated\""));
    }

    @Test
    void findPending_shouldMapEntitiesToDomain() {
        OutboxEventEntity entity = new OutboxEventEntity(
            UUID.randomUUID(), "Grade", UUID.randomUUID(), "GradeCreated",
            "{}", OutboxEventStatus.PENDING, Instant.now());
        when(repository.findByStatusOrderByCreatedAtAsc(eq(OutboxEventStatus.PENDING), any(Pageable.class)))
            .thenReturn(List.of(entity));

        List<OutboxEvent> result = adapter.findPending(10);

        assertEquals(1, result.size());
        assertEquals(entity.getId(), result.get(0).getId());
        assertEquals(OutboxEventStatus.PENDING, result.get(0).getStatus());
    }
}