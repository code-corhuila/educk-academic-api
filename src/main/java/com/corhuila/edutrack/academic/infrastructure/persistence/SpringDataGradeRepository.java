package com.corhuila.edutrack.academic.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataGradeRepository extends JpaRepository<GradeJpaEntity, UUID> {
    List<GradeJpaEntity> findByStudentId(UUID studentId);
}
