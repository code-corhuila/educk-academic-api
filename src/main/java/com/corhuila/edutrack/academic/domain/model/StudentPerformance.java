package com.corhuila.edutrack.academic.domain.model;

public class StudentPerformance {
    private final double average;
    private final boolean isPassing;

    public StudentPerformance(double average, boolean isPassing) {
        this.average = average;
        this.isPassing = isPassing;
    }

    public double getAverage() { return average; }
    public boolean isPassing() { return isPassing; }
}
