package com.corhuila.edutrack.academic.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class GradeCreatedEvent implements DomainEvent{
    private String eventId;
    private String eventType;
    private LocalDateTime occurredAt;
    private String correlationId;
    private String aggregateId;
    private GradeCreatedPayload payload;
    private int version;

    public GradeCreatedEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = "GradeCreated";
        this.occurredAt = LocalDateTime.now();
        this.correlationId = UUID.randomUUID().toString();
        this.version = 1;
    }

    public GradeCreatedEvent(String aggregateId, GradeCreatedPayload payload) {
        this();
        this.aggregateId = aggregateId;
        this.payload = payload;
    }

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public String getCorrelationId() { return correlationId; }
    public String getAggregateId() { return aggregateId; }
    public GradeCreatedPayload getPayload() { return payload; }
    public int getVersion() { return version; }

    public static class GradeCreatedPayload {
        private String gradeId;
        private String studentId;
        private String studentName;
        private String subjectId;
        private String subjectName;
        private String assignmentId;
        private String assignmentTitle;
        private Double grade;
        private String gradeScale;
        private String teacherId;
        private String teacherName;
        private String schoolId;
        private String schoolName;
        private LocalDateTime gradedAt;

        public GradeCreatedPayload(String gradeId, String studentId, String assignmentId, Double grade) {
            this.gradeId = gradeId;
            this.studentId = studentId;
            this.assignmentId = assignmentId;
            this.grade = grade;
            this.gradeScale = "1.0-5.0";
            this.gradedAt = LocalDateTime.now();
        }

        public String getGradeId() { return gradeId; }
        public String getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getSubjectId() { return subjectId; }
        public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public String getAssignmentId() { return assignmentId; }
        public String getAssignmentTitle() { return assignmentTitle; }
        public void setAssignmentTitle(String assignmentTitle) { this.assignmentTitle = assignmentTitle; }
        public Double getGrade() { return grade; }
        public String getGradeScale() { return gradeScale; }
        public String getTeacherId() { return teacherId; }
        public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
        public String getTeacherName() { return teacherName; }
        public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
        public String getSchoolId() { return schoolId; }
        public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
        public String getSchoolName() { return schoolName; }
        public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
        public LocalDateTime getGradedAt() { return gradedAt; }
    }
}
