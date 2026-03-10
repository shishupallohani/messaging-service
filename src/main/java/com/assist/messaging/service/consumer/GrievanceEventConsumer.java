package com.assist.messaging.service.consumer;

import com.assist.messaging.service.mail.GrievanceMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class GrievanceEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(GrievanceEventConsumer.class);
    private final GrievanceMailService grievanceMailService;

    public GrievanceEventConsumer(GrievanceMailService grievanceMailService) {
        this.grievanceMailService = grievanceMailService;
    }

    @KafkaListener(topics = "${app.kafka.topic.grievance-created}")
    public void consumeGrievanceCreatedEvent(
            GrievanceCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info(
                "Consumed grievance-created event key={} partition={} offset={} payload={}",
                key,
                partition,
                offset,
                event
        );

        grievanceMailService.sendGrievanceCreatedMail(event);
    }
}
