package com.corhuila.edutrack.academic.application.service;

import com.corhuila.edutrack.academic.domain.exception.InvalidGradeException;
import com.corhuila.edutrack.academic.domain.model.DomainEvent;
import com.corhuila.edutrack.academic.domain.model.Grade;
import com.corhuila.edutrack.academic.domain.port.out.GradeRepositoryPort;
import com.corhuila.edutrack.academic.domain.port.out.OutboxEventPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @Mock
    private GradeRepositoryPort gradeRepositoryPort;

    @Mock
    private OutboxEventPort outboxEventPort;

    @InjectMocks
    private GradeService gradeService;

    @Test
    void createGrade_shouldAppendGradeCreatedEventToOutbox() {
        when(gradeRepositoryPort.save(any(Grade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Grade created = gradeService.createGrade(UUID.randomUUID(), UUID.randomUUID(), 4.5, 1.0, "Well done");

        ArgumentCaptor<DomainEvent> captor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(outboxEventPort).append(eq("Grade"), captor.capture());
        assertEquals("GradeCreated", captor.getValue().getEventType());
        assertEquals(created.getId().toString(), captor.getValue().getAggregateId());
    }

    @Test
    void createGrade_withInvalidScore_shouldNotPersistNorAppendEvent() {
        assertThrows(InvalidGradeException.class,
            () -> gradeService.createGrade(UUID.randomUUID(), UUID.randomUUID(), 5.1, 1.0, null));

        verifyNoInteractions(gradeRepositoryPort, outboxEventPort);
    }
}
