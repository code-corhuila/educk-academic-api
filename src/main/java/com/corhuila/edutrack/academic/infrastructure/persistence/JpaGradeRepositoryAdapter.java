package com.corhuila.edutrack.academic.infrastructure.persistence;

import com.corhuila.edutrack.academic.domain.model.Grade;
import com.corhuila.edutrack.academic.domain.port.out.GradeRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaGradeRepositoryAdapter implements GradeRepositoryPort {

    private final SpringDataGradeRepository repository;

    public JpaGradeRepositoryAdapter(SpringDataGradeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Grade save(Grade grade) {
        GradeJpaEntity entity = new GradeJpaEntity(
            grade.getId(),
            grade.getStudentId(),
            grade.getAssignmentId(),
            grade.getScore(),
            grade.getWeight(),
            grade.getFeedback(),
            grade.getCreatedAt()
        );
        GradeJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Grade> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Grade> findByStudentId(UUID studentId) {
        return repository.findByStudentId(studentId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Grade> findAll() {
        return repository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private Grade toDomain(GradeJpaEntity entity) {
        return new Grade(
            entity.getId(),
            entity.getStudentId(),
            entity.getAssignmentId(),
            entity.getScore(),
            entity.getWeight(),
            entity.getFeedback(),
            entity.getCreatedAt()
        );
    }
}
