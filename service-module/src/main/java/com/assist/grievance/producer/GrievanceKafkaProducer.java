package com.assist.grievance.producer;


import com.assist.grievance.data.event.GrievanceCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrievanceKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topic.grievance-created}")
    private String grievanceCreatedTopic;

    public void publishGrievanceCreated(GrievanceCreatedEvent event) {
        kafkaTemplate.send(grievanceCreatedTopic, event.getGrievanceNo(), event);
        log.info("Published grievance-created event for grievanceNo={}", event.getGrievanceNo());
    }
}
