package com.corhuila.edutrack.academic.domain.port.out;

import com.corhuila.edutrack.academic.domain.model.Grade;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GradeRepositoryPort {
    Grade save(Grade grade);
    Optional<Grade> findById(UUID id);
    List<Grade> findByStudentId(UUID studentId);
    List<Grade> findAll();
}
