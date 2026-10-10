package com.corhuila.edutrack.academic.domain.port.in;

import com.corhuila.edutrack.academic.domain.model.Grade;
import com.corhuila.edutrack.academic.domain.model.StudentPerformance;
import java.util.List;
import java.util.UUID;

public interface GetGradesUseCase {
    List<Grade> getGradesByStudent(UUID studentId);
    List<Grade> getAllGrades();
    StudentPerformance getStudentPerformance(UUID studentId);
}
