package com.corhuila.edutrack.academic.infrastructure.web.dto;

public class StudentPerformanceResponse {
    private Double averageScore;
    private boolean passed;

    public StudentPerformanceResponse(Double averageScore, boolean passed) {
        this.averageScore = averageScore;
        this.passed = passed;
    }

    public Double getAverageScore() { return averageScore; }
    public boolean isPassed() { return passed; }
}
