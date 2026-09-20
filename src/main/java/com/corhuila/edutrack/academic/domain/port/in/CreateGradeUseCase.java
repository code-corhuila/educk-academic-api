package com.corhuila.edutrack.academic.domain.port.in;

import com.corhuila.edutrack.academic.domain.model.Grade;
import java.util.UUID;

public interface CreateGradeUseCase {
    Grade createGrade(UUID studentId, UUID assignmentId, Double score, String feedback);
}
