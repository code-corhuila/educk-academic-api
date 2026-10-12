package com.corhuila.edutrack.academic.infrastructure.web;

import com.corhuila.edutrack.academic.domain.exception.InvalidGradeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GradeExceptionHandler {
    @ExceptionHandler(InvalidGradeException.class)
    ResponseEntity<?> invalid(InvalidGradeException error) {
        return ResponseEntity.badRequest().body(Map.of("error", error.getMessage()));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> conflict() {
        return ResponseEntity.status(409).body(Map.of("error", "Grade conflicts with existing academic data"));
    }
}
