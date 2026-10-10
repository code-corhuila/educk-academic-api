package com.corhuila.edutrack.academic.application.service;

import com.corhuila.edutrack.academic.domain.exception.InvalidGradeException;
import com.corhuila.edutrack.academic.domain.model.Grade;
import com.corhuila.edutrack.academic.domain.model.GradeCreatedEvent;
import com.corhuila.edutrack.academic.domain.port.in.CreateGradeUseCase;
import com.corhuila.edutrack.academic.domain.port.in.GetGradesUseCase;
import com.corhuila.edutrack.academic.domain.port.out.GradeRepositoryPort;
import com.corhuila.edutrack.academic.domain.port.out.OutboxEventPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GradeService implements CreateGradeUseCase, GetGradesUseCase {

    private static final String GRADE_AGGREGATE_TYPE = "Grade";
    private static final double MIN_SCORE = 0.0;
    private static final double MAX_SCORE = 5.0;

    private final GradeRepositoryPort gradeRepositoryPort;
    private final OutboxEventPort outboxEventPort;

    public GradeService(GradeRepositoryPort gradeRepositoryPort, OutboxEventPort outboxEventPort) {
        this.gradeRepositoryPort = gradeRepositoryPort;
        this.outboxEventPort = outboxEventPort;
    }

    /**
     * Saves the grade and its GradeCreated event in ONE local transaction (ADR-007).
     * Either both are committed or both are rolled back, so no event is ever lost.
     */
    @Override
    @Transactional
    public Grade createGrade(UUID studentId, UUID assignmentId, Double score, Double weight, String feedback) {
        validateScore(score);

        Grade grade = new Grade(
            UUID.randomUUID(),
            studentId,
            assignmentId,
            score,
            weight,
            feedback,
            LocalDateTime.now()
        );

        Grade saved = gradeRepositoryPort.save(grade);

        // No direct broker call: the event is stored in the outbox and relayed asynchronously
        outboxEventPort.append(GRADE_AGGREGATE_TYPE, buildGradeCreatedEvent(saved));

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

    @Transactional(readOnly = true)
    public com.corhuila.edutrack.academic.domain.model.StudentPerformance getStudentPerformance(UUID studentId) {
        List<Grade> grades = gradeRepositoryPort.findByStudentId(studentId);
        if (grades.isEmpty()) {
            return new com.corhuila.edutrack.academic.domain.model.StudentPerformance(0.0, false);
        }
        double sumProduct = 0.0;
        double sumWeights = 0.0;
        for (Grade g : grades) {
            double w = g.getWeight() != null ? g.getWeight() : 1.0;
            sumProduct += (g.getScore() * w);
            sumWeights += w;
        }
        double average = sumWeights > 0 ? sumProduct / sumWeights : 0.0;
        return new com.corhuila.edutrack.academic.domain.model.StudentPerformance(average, average >= 3.0);
    }

    private void validateScore(Double score) {
        if (score == null || score < MIN_SCORE || score > MAX_SCORE) {
            throw new InvalidGradeException("Score must be between 0.0 and 5.0 according to academic scale");
        }
    }

    private GradeCreatedEvent buildGradeCreatedEvent(Grade grade) {
        GradeCreatedEvent.GradeCreatedPayload payload = new GradeCreatedEvent.GradeCreatedPayload(
            grade.getId().toString(),
            grade.getStudentId().toString(),
            grade.getAssignmentId().toString(),
            grade.getScore()
        );
        return new GradeCreatedEvent(grade.getId().toString(), payload);
    }
}
