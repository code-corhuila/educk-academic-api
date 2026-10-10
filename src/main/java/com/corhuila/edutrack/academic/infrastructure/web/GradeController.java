package com.corhuila.edutrack.academic.infrastructure.web;

import com.corhuila.edutrack.academic.domain.model.Grade;
import com.corhuila.edutrack.academic.domain.port.in.CreateGradeUseCase;
import com.corhuila.edutrack.academic.domain.port.in.GetGradesUseCase;
import com.corhuila.edutrack.academic.infrastructure.web.dto.CreateGradeRequest;
import com.corhuila.edutrack.academic.infrastructure.web.dto.GradeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/grades")
@CrossOrigin(origins = "*")
public class GradeController {

    private final CreateGradeUseCase createGradeUseCase;
    private final GetGradesUseCase getGradesUseCase;

    public GradeController(CreateGradeUseCase createGradeUseCase, GetGradesUseCase getGradesUseCase) {
        this.createGradeUseCase = createGradeUseCase;
        this.getGradesUseCase = getGradesUseCase;
    }

    @PostMapping
    public ResponseEntity<GradeResponse> createGrade(@RequestBody CreateGradeRequest request) {
        Grade created = createGradeUseCase.createGrade(
            request.getStudentId(),
            request.getAssignmentId(),
            request.getScore(),
            request.getWeight(),
            request.getFeedback()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(GradeResponse.fromDomain(created));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeResponse>> getGradesByStudent(@PathVariable UUID studentId) {
        List<Grade> grades = getGradesUseCase.getGradesByStudent(studentId);
        return ResponseEntity.ok(grades.stream().map(GradeResponse::fromDomain).collect(Collectors.toList()));
    }

    @GetMapping("/student/{studentId}/performance")
    public ResponseEntity<com.corhuila.edutrack.academic.infrastructure.web.dto.StudentPerformanceResponse> getStudentPerformance(@PathVariable UUID studentId) {
        com.corhuila.edutrack.academic.domain.model.StudentPerformance performance = getGradesUseCase.getStudentPerformance(studentId);
        return ResponseEntity.ok(new com.corhuila.edutrack.academic.infrastructure.web.dto.StudentPerformanceResponse(performance.getAverage(), performance.isPassing()));
    }

    @GetMapping
    public ResponseEntity<List<GradeResponse>> getAllGrades() {
        List<Grade> grades = getGradesUseCase.getAllGrades();
        return ResponseEntity.ok(grades.stream().map(GradeResponse::fromDomain).collect(Collectors.toList()));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK - Academic Service (HU-001)");
    }
}
