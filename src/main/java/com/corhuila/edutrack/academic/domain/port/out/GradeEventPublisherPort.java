package com.corhuila.edutrack.academic.domain.port.out;

import com.corhuila.edutrack.academic.domain.model.GradeCreatedEvent;

public interface GradeEventPublisherPort {
    void publishGradeCreated(GradeCreatedEvent event);
}
