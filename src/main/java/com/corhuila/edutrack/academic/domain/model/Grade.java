package com.corhuila.edutrack.academic.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Grade {
    private UUID id;
    private UUID studentId;
    private UUID assignmentId;
    private Double score;
    private String feedback;
    private LocalDateTime createdAt;

    public Grade(UUID id, UUID studentId, UUID assignmentId, Double score, String feedback, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.score = score;
        this.feedback = feedback;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getStudentId() { return studentId; }
    public UUID getAssignmentId() { return assignmentId; }
    public Double getScore() { return score; }
    public String getFeedback() { return feedback; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
