package com.corhuila.edutrack.academic.infrastructure.amqp;

import com.corhuila.edutrack.academic.domain.model.GradeCreatedEvent;
import com.corhuila.edutrack.academic.domain.port.out.GradeEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQGradeEventPublisher implements GradeEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQGradeEventPublisher.class);
    private static final String EXCHANGE_NAME = "edutrack.events";
    private static final String ROUTING_KEY = "academic.grade.created";

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQGradeEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishGradeCreated(GradeCreatedEvent event) {
        try {
            log.info("Publishing GradeCreated AMQP event: aggregateId={}, studentId={}", 
                     event.getAggregateId(), event.getPayload().getStudentId());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event.getPayload().getGradeId());
        } catch (Exception e) {
            log.warn("AMQP broker unavailable, event logged locally: {}", e.getMessage());
        }
    }
}
