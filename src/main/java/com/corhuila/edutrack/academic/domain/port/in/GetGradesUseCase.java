package com.corhuila.edutrack.academic.domain.port.in;

import com.corhuila.edutrack.academic.domain.model.Grade;
import java.util.List;
import java.util.UUID;

public interface GetGradesUseCase {
    List<Grade> getGradesByStudent(UUID studentId);
    List<Grade> getAllGrades();
}
