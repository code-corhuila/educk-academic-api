package com.corhuila.edutrack.academic.infrastructure.web.dto;

import java.util.UUID;

public class CreateGradeRequest {
    private UUID studentId;
    private UUID assignmentId;
    private Double score;
    private Double weight;
    private String feedback;

    public CreateGradeRequest() {}

    public CreateGradeRequest(UUID studentId, UUID assignmentId, Double score, Double weight, String feedback) {
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.score = score;
        this.weight = weight;
        this.feedback = feedback;
    }

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }
    public UUID getAssignmentId() { return assignmentId; }
    public void setAssignmentId(UUID assignmentId) { this.assignmentId = assignmentId; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
