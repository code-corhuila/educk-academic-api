package com.corhuila.edutrack.academic.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "grades")
public class GradeJpaEntity {

    @Id
    private UUID id;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "assignment_id", nullable = false)
    private UUID assignmentId;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public GradeJpaEntity() {}

    public GradeJpaEntity(UUID id, UUID studentId, UUID assignmentId, Double score, String feedback, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.score = score;
        this.feedback = feedback;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }
    public UUID getAssignmentId() { return assignmentId; }
    public void setAssignmentId(UUID assignmentId) { this.assignmentId = assignmentId; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
