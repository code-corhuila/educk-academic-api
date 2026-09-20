package com.corhuila.edutrack.academic.application.service;

import com.corhuila.edutrack.academic.domain.exception.InvalidGradeException;
import com.corhuila.edutrack.academic.domain.model.Grade;
import com.corhuila.edutrack.academic.domain.model.GradeCreatedEvent;
import com.corhuila.edutrack.academic.domain.port.in.CreateGradeUseCase;
import com.corhuila.edutrack.academic.domain.port.in.GetGradesUseCase;
import com.corhuila.edutrack.academic.domain.port.out.GradeEventPublisherPort;
import com.corhuila.edutrack.academic.domain.port.out.GradeRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GradeService implements CreateGradeUseCase, GetGradesUseCase {

    private final GradeRepositoryPort gradeRepositoryPort;
    private final GradeEventPublisherPort gradeEventPublisherPort;

    public GradeService(GradeRepositoryPort gradeRepositoryPort, GradeEventPublisherPort gradeEventPublisherPort) {
        this.gradeRepositoryPort = gradeRepositoryPort;
        this.gradeEventPublisherPort = gradeEventPublisherPort;
    }

    @Override
    @Transactional
    public Grade createGrade(UUID studentId, UUID assignmentId, Double score, String feedback) {
        if (score == null || score < 0.0 || score > 5.0) {
            throw new InvalidGradeException("Score must be between 0.0 and 5.0 according to academic scale");
        }

        Grade grade = new Grade(
            UUID.randomUUID(),
            studentId,
            assignmentId,
            score,
            feedback,
            LocalDateTime.now()
        );

        Grade saved = gradeRepositoryPort.save(grade);

        // Publish GradeCreated domain event to AMQP bus
        GradeCreatedEvent.GradeCreatedPayload payload = new GradeCreatedEvent.GradeCreatedPayload(
            saved.getId().toString(),
            saved.getStudentId().toString(),
            saved.getAssignmentId().toString(),
            saved.getScore()
        );
        GradeCreatedEvent event = new GradeCreatedEvent(saved.getId().toString(), payload);
        gradeEventPublisherPort.publishGradeCreated(event);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grade> getGradesByStudent(UUID studentId) {
        return gradeRepositoryPort.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Grade> getAllGrades() {
        return gradeRepositoryPort.findAll();
    }
}
