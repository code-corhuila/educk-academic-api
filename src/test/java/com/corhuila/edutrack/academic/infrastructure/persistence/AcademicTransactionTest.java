package com.corhuila.edutrack.academic.infrastructure.persistence;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import com.corhuila.edutrack.academic.application.service.GradeService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@Testcontainers
class AcademicTransactionTest {
    @Container static PostgreSQLContainer<?> database = database();
    static PostgreSQLContainer<?> database() {
        var container = new PostgreSQLContainer<>("postgres:16-alpine").withPassword(UUID.randomUUID().toString());
        String root = System.getProperty("academic.db.path");
        if (root == null) throw new IllegalStateException("Set -Dacademic.db.path to educk-academic-db");
        int index = 0;
        for (String file : List.of("01_ddl/V1.1__create_academic_schema.sql", "01_ddl/V1.2__create_academic_tables.sql",
                "04_alter/V1.3__add_academic_foreign_keys.sql", "04_alter/V2.2__add_weight_to_grades.sql",
                "04_alter/V2.3__align_grade_precision.sql")) {
            container.withCopyFileToContainer(MountableFile.forHostPath(Path.of(root, file)),
                    "/docker-entrypoint-initdb.d/0" + index++ + ".sql");
        }
        return container;
    }
    @DynamicPropertySource static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
        registry.add("jwt.secret", () -> UUID.randomUUID().toString() + UUID.randomUUID());
    }
    @Autowired GradeService service;
    @Autowired JdbcTemplate jdbc;
    @SpyBean JpaOutboxEventAdapter outbox;
    @AfterEach void clearAuthentication() { SecurityContextHolder.clearContext(); }

    @Test void savesOwnedAssignmentAndRollsBackOnOutboxFailure() {
        UUID teacher = UUID.randomUUID(), subject = UUID.randomUUID(), assignment = UUID.randomUUID();
        jdbc.update("INSERT INTO academic_schema.subjects(id,name,school_id,teacher_id) VALUES (?,?,?,?)",
                subject, "Integration subject", UUID.randomUUID(), teacher);
        jdbc.update("INSERT INTO academic_schema.assignments(id,subject_id,title) VALUES (?,?,?)",
                assignment, subject, "Integration assignment");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(teacher.toString(), null, List.of()));
        var grade = service.createGrade(UUID.randomUUID(), assignment, 4.25, 0.25, "Verified");
        assertEquals(4.25, service.getGradesByStudent(grade.getStudentId()).getFirst().getScore());
        assertEquals(teacher, jdbc.queryForObject("SELECT graded_by FROM academic_schema.grades WHERE id = ?", UUID.class, grade.getId()));
        assertEquals(1, jdbc.queryForObject("SELECT COUNT(*) FROM academic_schema.outbox_events WHERE status = 'PENDING'", Integer.class));
        service.createGrade(UUID.randomUUID(), assignment, 0.0, 1.0, null);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(UUID.randomUUID().toString(), null, List.of()));
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> service.createGrade(UUID.randomUUID(), assignment, 4.0, 1.0, null));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(teacher.toString(), null, List.of()));
        JpaOutboxEventAdapter target = org.springframework.test.util.AopTestUtils.getUltimateTargetObject(outbox);
        doThrow(new IllegalStateException("Injected outbox failure")).when(target).append(any(), any());
        assertThrows(IllegalStateException.class, () -> service.createGrade(UUID.randomUUID(), assignment, 3.5, 1.0, null));
        assertEquals(2, jdbc.queryForObject("SELECT COUNT(*) FROM academic_schema.grades", Integer.class));
        assertEquals(2, jdbc.queryForObject("SELECT COUNT(*) FROM academic_schema.outbox_events", Integer.class));
    }
}
