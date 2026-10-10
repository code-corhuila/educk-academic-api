package com.corhuila.edutrack.academic.infrastructure.web.dto;

import com.corhuila.edutrack.academic.domain.model.Grade;
import java.time.LocalDateTime;
import java.util.UUID;

public class GradeResponse {
    private UUID id;
    private UUID studentId;
    private UUID assignmentId;
    private Double score;
    private Double weight;
    private String feedback;
    private LocalDateTime createdAt;

    public GradeResponse(UUID id, UUID studentId, UUID assignmentId, Double score, Double weight, String feedback, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.score = score;
        this.weight = weight;
        this.feedback = feedback;
        this.createdAt = createdAt;
    }

    public static GradeResponse fromDomain(Grade grade) {
        return new GradeResponse(
            grade.getId(),
            grade.getStudentId(),
            grade.getAssignmentId(),
            grade.getScore(),
            grade.getWeight(),
            grade.getFeedback(),
            grade.getCreatedAt()
        );
    }

    public UUID getId() { return id; }
    public UUID getStudentId() { return studentId; }
    public UUID getAssignmentId() { return assignmentId; }
    public Double getScore() { return score; }
    public Double getWeight() { return weight; }
    public String getFeedback() { return feedback; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
